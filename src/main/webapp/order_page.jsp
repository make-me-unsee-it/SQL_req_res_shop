<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.step.hryshkin.model.User"%>

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

    <h3 align="center">Здравствуйте, <%= ((User) request.getSession().getAttribute("user")).getLogin() %>!</h3>

<form action="/order_page.jsp" method="get">
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
    <p>Ваша корзина:</p>
    <p> тут будут отрисованы товары с for each</p>
</div>



    <form action="/user_check_page.jsp" method="post">
        <p align="center"><input type="submit" value="вернуться на главную"></p>
    </form>

</body>
</html>