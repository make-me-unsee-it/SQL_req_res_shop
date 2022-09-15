<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.step.hryshkin.model.User"%>
<%@ page import="com.step.hryshkin.utils.UtilsForOnlineShop"%>
<%@ page import="com.step.hryshkin.service.impl.GoodServiceImpl"%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>СОЗДАНО В УЧЕБНЫХ ЦЕЛЯХ</title>
<style type="text/css">
body {
background: gray;
}
</style>
</head>

<body>
<h3 align="center">Здравствуйте, <%= ((User) request.getSession().getAttribute("user")).getUserName() %>!</h3>

<form action="/order_page.jsp" method="post">
        <p align="center">
            <label>
            <select name="select" size="1">
                <c:forEach var="item" items="${goods}">
                    <option value="${item.getId()}"> ${item.getTitle()} ${item.getPrice()}$</option>
                </c:forEach>
            </select>
             <input type="submit" value="Add item">
            </label>
        </p>
    </form>

<div align="center">
    <h4>Ваша корзина:</h4>
         <ol style="display: table; margin:0 auto;">
            <c:forEach items="${UtilsForOnlineShop.printGoodsForCurrentOrder(order.getId())}" var="item" >
                <li>${item}</li>
            </c:forEach>
         </ol>
</div>
    <form action="/basket_page.jsp" method="post">
        <p align="center"><input type="submit" value="заказать"></p>
    </form>
</body>
</html>