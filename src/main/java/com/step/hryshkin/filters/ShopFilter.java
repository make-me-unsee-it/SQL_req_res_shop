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

        // здесь добавляется goods для for each на странице реализации
        request.setAttribute("goods", goodDAO.getAll());

        // здесь происходит проверка входа юзера (а также создание нового юзера в базу если нужно)
        checkUser((HttpServletRequest) servletRequest);

        // проверяем, согласился ли юзер с условиями пользования магазином
        checkFlag(servletResponse, request);

        // здесь реализация добавления заказа в Order и OrderGood
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
        String login = request.getParameter("username"); //возможен null
        String password = request.getParameter("password"); //возможен null
        User user = new User(login, password); // этот Юзер существует только тут в методе
        if (login != null) { // если логин не пустой (или мы не со второй страницы пришли) то...
            if (userDAO.getUserByName(login).isEmpty()) { // если юзера с таким именем нет в БД...
                userDAO.createNewUser(user); // запишем его в БД
            }
            Optional<User> newUser = userDAO.getUserByName(login); // получаем Юзера из БД

            if (newUser.isPresent()) { // если полученный Юзер не null
                if (request.getSession().getAttribute("user") == null) { // атрибуту сессии user вписываем
                    UtilsForOnlineShop.setUser(request, newUser.get());     // значение - пришел новый


                } else if (!UtilsForOnlineShop.isUsersEquals(request)) { // и если новый юзер - с новым именем
                    request.getSession().invalidate();                   // то закрываем старую сессию
                    UtilsForOnlineShop.setUser(request, newUser.get());  // и пишем в сессию нового юзера
                }
            }
        }
    }

    private void checkForNewOrder(HttpServletRequest request) {
        if (request.getParameter("select") != null) {
            long currentOrderId = Long.parseLong(request.getParameter("select"));
            User currentUser = (User) request.getSession().getAttribute("user");
            Optional<Good> currentGood = goodDAO.getById(currentOrderId);
            if (currentGood.isPresent()) {
                BigDecimal currentGoodPrice = currentGood.get().getPrice();
                Order order = new Order(currentUser.getId(), currentGoodPrice);
                orderDAO.createNewOrder(order);                             // создали Order

                long currentGoodId = currentGood.get().getId();
                Optional<Order> lastOrder = orderDAO.getLastOrder();
                if (lastOrder.isPresent()) {
                    OrderGood orderGood = new OrderGood(lastOrder.get().getId(), currentGoodId);
                    orderGoodDAO.createNewOrderGoodDAO(orderGood);  // создали OrderGood
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
                } catch (ServletException e) {
                    LOGGER.error("ServletException in checkFlag");
                } catch (IOException e) {
                    LOGGER.error("IOException in checkFlag");
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
