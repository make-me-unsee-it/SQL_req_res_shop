package com.step.hryshkin.utils;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.model.Order;
import com.step.hryshkin.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UtilsForOnlineShop {
    private static final GoodDAO goodDAO = new GoodDAOImpl();
    private UtilsForOnlineShop() {
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }

    public static User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    public static void setGoods(HttpServletRequest request, List<Good> goods) {
        request.getSession().setAttribute("goods", goods);
    }

    //TODO снести к херам. Он только в саут-чек используется
    public static List<Good> getGoods(HttpServletRequest request) {
        List<Good> goods = null;
        goods = (List<Good>) request.getSession().getAttribute("goods");
        System.out.println("Это метод getGoods в утилитах");
        System.out.println(goods);
        return (List<Good>) request.getSession().getAttribute("goods");
    }

    public static void setOrder(HttpServletRequest request, Order order) {
        request.getSession().setAttribute("order", order);
    }

    public static Order getOrder(HttpServletRequest request) {
        return (Order) request.getSession().getAttribute("order");
    }

    public static boolean isUsersEquals(HttpServletRequest request) {
        return ((User) request.getSession().getAttribute("user"))
                .getUserName().equals(request.getParameter("username"));
    }

    public static boolean isOrdersEqualsById(HttpServletRequest request, long id) {
        return ((Order) request.getSession().getAttribute("order"))
                .getId().equals(id);
    }

    public static void setCheckStatus(HttpServletRequest request, String check) {
        request.getSession().setAttribute("check", check);
    }

    public static List<String> printGoodsForCurrentUser(String name) {
        return goodDAO.getGoodBasketByUserName(name);
    }

    public static List<String> printGoodsForCurrentOrder(long id) {
        return goodDAO.getGoodListByOrderId(id);
    }

    public static void stopShopping(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
