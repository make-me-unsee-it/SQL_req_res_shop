package com.step.hryshkin.filters;


import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.dao.impl.UserDAOImpl;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.model.User;
import com.step.hryshkin.utils.UtilsForOnlineShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;


public class UserPassFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(UserPassFilter.class);
    private final UserDAO userDAO = new UserDAOImpl();
    private final GoodDAO goodDAO = new GoodDAOImpl();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;


        // здесь добавляется goods для for each на странице реализации
        request.setAttribute("goods", goodDAO.getAll());

        // здесь происходит чек на создание нового юзера в базу
        checkUser((HttpServletRequest) servletRequest);

        // проверяем, согласился ли пользователь с условиями пользования магазином
        checkFlag(servletResponse, request);

        // здесь будет реализация корзины
        //orderCreation(request);

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

    //private void orderCreation (HttpServletRequest request) {
    //}


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
