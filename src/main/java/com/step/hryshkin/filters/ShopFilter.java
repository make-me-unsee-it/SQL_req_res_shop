package com.step.hryshkin.filters;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.OrderDAO;
import com.step.hryshkin.dao.OrderGoodDAO;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.dao.impl.OrderDAOImpl;
import com.step.hryshkin.dao.impl.OrderGoodDAOImpl;
import com.step.hryshkin.dao.impl.UserDAOImpl;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.model.Order;
import com.step.hryshkin.model.OrderGood;
import com.step.hryshkin.model.User;
import com.step.hryshkin.utils.UtilsForOnlineShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class ShopFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(ShopFilter.class);
    private final UserDAO userDAO = new UserDAOImpl();
    private final GoodDAO goodDAO = new GoodDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    private final OrderGoodDAO orderGoodDAO = new OrderGoodDAOImpl();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setAttribute("goods", goodDAO.getAll());
        checkUser((HttpServletRequest) servletRequest);
        checkFlag(servletResponse, request);
        checkForNewOrder((HttpServletRequest) servletRequest);
        try {
            filterChain.doFilter(request, servletResponse);
        } catch (IOException e) {
            LOGGER.error("IOException in doFilter " + e);
        } catch (ServletException e) {
            LOGGER.error("ServletException in doFilter " + e);
        }
    }

    private void checkUser(HttpServletRequest request) {
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(login, password);
        if (login != null) {
            if (userDAO.getUserByName(login).isEmpty()) {
                userDAO.createNewUser(user);
            }
            Optional<User> newUser = userDAO.getUserByName(login);
            if (newUser.isPresent()) {
                if (request.getSession().getAttribute("user") == null) {
                    UtilsForOnlineShop.setUser(request, newUser.get());
                } else if (!UtilsForOnlineShop.isUsersEquals(request)) {
                    request.getSession().invalidate();
                    UtilsForOnlineShop.setUser(request, newUser.get());
                }
            }
        }
    }

    private void checkForNewOrder(HttpServletRequest request) {
        if (request.getParameter("select") != null) {
            Long userId = ((User) request.getSession().getAttribute("user")).getId();
            long goodId = Long.parseLong(request.getParameter("select"));
            Optional<Good> currentGood = goodDAO.getById(goodId);
            if (currentGood.isPresent()) {
                if (request.getSession().getAttribute("order") != null) {
                    long currentOrderId = ((Order) request.getSession().getAttribute("order")).getId();
                    BigDecimal currentOrderTotalPrice =
                            ((Order) request.getSession().getAttribute("order")).getTotalPrice();
                    currentOrderTotalPrice = currentOrderTotalPrice.add(currentGood.get().getPrice());
                    Order order = new Order(currentOrderId, userId, currentOrderTotalPrice);
                    orderDAO.updateOrder(order);
                    UtilsForOnlineShop.setOrder(request, order);
                    OrderGood orderGood = new OrderGood(currentOrderId, goodId);
                    orderGoodDAO.createNewOrderGoodDAO(orderGood);
                }

                if (request.getSession().getAttribute("order") == null) {
                    Order order = new Order(userId, currentGood.get().getPrice());
                    orderDAO.createNewOrder(order);
                    Optional<Order> newestOrder = orderDAO.getLastOrder();
                    if (newestOrder.isPresent()) {
                        UtilsForOnlineShop.setOrder(request, newestOrder.get());
                        long orderId = newestOrder.get().getId();
                        OrderGood orderGood = new OrderGood(orderId, goodId);
                        orderGoodDAO.createNewOrderGoodDAO(orderGood);
                    }
                }
            }
        }
    }

    private void checkFlag(ServletResponse servletResponse, HttpServletRequest request) {
        if (request.getSession().getAttribute("check") == null) {
            if (request.getParameter("check") != null) {
                UtilsForOnlineShop.setCheckStatus(request, request.getParameter("check"));
            } else {
                String path = "/terms_of_use_error.jsp";
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
                try {
                    requestDispatcher.forward(request, servletResponse);
                } catch (ServletException exception) {
                    LOGGER.error("ServletException in checkFlag " + exception);
                } catch (IOException exception) {
                    LOGGER.error("IOException in checkFlag " + exception);
                }
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
