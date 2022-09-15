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
                System.out.println("ЧЕК НОМЕР 1");
        UtilsForOnlineShop.setGoods(request, goodDAO.getAll());
                System.out.println("ЧЕК НОМЕР 2");
                System.out.println("вот он" + goodDAO.getAll());
                System.out.println("ЧЕК НОМЕР 2 с половиной");
                System.out.println(UtilsForOnlineShop.getGoods(request));
                System.out.println("ЧЕК НОМЕР 3");
                System.out.println(request.getSession().getAttribute("goods").toString());
                System.out.println("ЧЕК НОМЕР 4");
        // TODO до сюда все сработало, странно

        checkFlag(servletResponse, request);
        // TODO переставил проверку флага выше юзера. Думаю, это правильно

        checkUser((HttpServletRequest) servletRequest);
        // TODO до сюда вроде тоже работет, но проверил не все ситуации

        checkForNewOrder((HttpServletRequest) servletRequest);


        try {
            filterChain.doFilter(request, servletResponse);
        } catch (IOException e) {
            LOGGER.error("IOException in doFilter " + e);
        } catch (ServletException e) {
            LOGGER.error("ServletException in doFilter " + e);
        }
    }

    //МЕТОД ПРОВЕРЕН НА СОЗДАНИЕ НОВОГО ЮЗЕРА. ПРОВЕРИТЬ СТАРОГО НЕ ПОМЕШАЕТ
    private void checkUser(HttpServletRequest request) {
        System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 1");
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(login, password);
        System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 2");
        if (login != null) {
            if (userDAO.getUserByName(login).isEmpty()) {
                System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 2-1!!!!");
                userDAO.createNewUser(user);
                System.out.println("в checkUser() создан новый Юзер в таблице. Точка 2-2!!!!");
            }
            System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 3");
            Optional<User> newUser = userDAO.getUserByName(login);
            System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 4");
            if (newUser.isPresent()) {
                System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 5");
                if (request.getSession().getAttribute("user") == null) {
                    System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 5-1");
                    UtilsForOnlineShop.setUser(request, newUser.get());
                    System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 5-2");
                } else if (!UtilsForOnlineShop.isUsersEquals(request)) {
                    System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 6-1");
                    request.getSession().invalidate();
                    UtilsForOnlineShop.setUser(request, newUser.get());
                    System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. Точка 6-2");
                }
            }
        }
        System.out.println("ПРОВЕРЯЕМ checkUser() в фильтре. ВЫХОДИМ ИЗ МЕТОДА");
    }


    private void checkForNewOrder(HttpServletRequest request) {
            System.out.println("Начинаем проверку checkForNewOrder()");
        if (request.getParameter("select") != null) {
            Long userId = ((User) request.getSession().getAttribute("user")).getId();
                System.out.println("Точка 1 - ЮзерАйди = " + userId);
            long goodId = Long.parseLong(request.getParameter("select"));
                System.out.println("Точка 2 - ГудАйди = " + goodId);

            Optional<Good> currentGood = goodDAO.getById(goodId);
                System.out.println("Точка 3 - товар получен");
            if (currentGood.isPresent()) {
                System.out.println("Точка 4 - проверка начал ли собираться заказ - чтбы знать его Айди");

                if (request.getSession().getAttribute("order") != null) {
                    System.out.println("Точка 6 - тут мы при втором и последующем выбранном товаре");
                    long currentOrderId = ((Order) request.getSession().getAttribute("order")).getId();
                    System.out.println("номер текущего ордера = " + currentOrderId);

                    BigDecimal currentOrderTotalPrice =
                            ((Order) request.getSession().getAttribute("order")).getTotalPrice();
                    System.out.println("текущий заказ стоил = " + currentOrderTotalPrice);
                    currentOrderTotalPrice = currentOrderTotalPrice.add(currentGood.get().getPrice());
                    System.out.println("а теперь стоит = " + currentOrderTotalPrice);

                    Order order = new Order(currentOrderId, userId, currentOrderTotalPrice);
                    orderDAO.updateOrder(order);
                    System.out.println("(фильтр) создан новый ордер - проверить в h2 ");
                    UtilsForOnlineShop.setOrder(request, order);
                    System.out.println("засетали обновленный ордер в сессию");
                    OrderGood orderGood = new OrderGood(currentOrderId, goodId);
                    orderGoodDAO.createNewOrderGoodDAO(orderGood);
                }

                if (request.getSession().getAttribute("order") == null) {
                    System.out.println("Точка 5 - при первом выбранном товаре должны быть здесь");
                    Order order = new Order(userId, currentGood.get().getPrice());
                    System.out.println("Точка 5-1");
                    orderDAO.createNewOrder(order);
                    //TODO каменты. Не проверял как, но эта хрень отработала
                    System.out.println("Точка 5-2");
                    Optional<Order> newestOrder = orderDAO.getLastOrder();
                    if (newestOrder.isPresent()) {
                        System.out.println("Точка 5-3");
                        UtilsForOnlineShop.setOrder(request, newestOrder.get());
                        System.out.println("Точка 5-4");
                        long orderId = newestOrder.get().getId();
                        System.out.println("Точка 5-5. orderId = " + orderId);
                        OrderGood orderGood = new OrderGood(orderId, goodId);
                        orderGoodDAO.createNewOrderGoodDAO(orderGood);
                        System.out.println("Точка 5-6. Создан новый ОрдерГуд - проверить в h2");
                    }
                }
            }
        }
        System.out.println("Заканчиваю проверку checkForNewOrder()");
    }

    private void checkFlag(ServletResponse servletResponse, HttpServletRequest request) {
        System.out.println("ПРОВЕРКА ЧЕК-ФЛАГ 1");
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
        System.out.println("ПРОВЕРКА ЧЕК-ФЛАГ - ВЫПОЛНЕНА");
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
