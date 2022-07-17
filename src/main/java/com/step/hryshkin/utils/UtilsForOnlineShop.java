package com.step.hryshkin.utils;

import com.step.hryshkin.model.User;

import javax.servlet.http.HttpServletRequest;

public class UtilsForOnlineShop {

    private UtilsForOnlineShop() {
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }

    public static boolean isUsersEquals(HttpServletRequest request) {
        return ((User) request.getSession().getAttribute("user"))
                .getLogin().equals(request.getParameter("username"));
    }

    public static void setCheckStatus(HttpServletRequest request, String check) {
        request.getSession().setAttribute("check", check);
    }
}
