<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.step.hryshkin.model.User"%>
<%@ page import="com.step.hryshkin.model.Order"%>
<%@ page import="com.step.hryshkin.utils.UtilsForOnlineShop"%>
<%@ page import="com.step.hryshkin.service.impl.OrderServiceImpl"%>

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

<div align="center">
    <h4>Ваш заказ:</h4>
         <ol style="display: table; margin:0 auto;">
            <c:forEach items="${UtilsForOnlineShop.printGoodsForCurrentOrder(order.getId())}" var="item" >
                <li>${item}</li>
            </c:forEach>
         </ol>
    <h4>Сумма: <%= new OrderServiceImpl().printTotalPriceForOrder(((Order) request.getSession().getAttribute("order")).getId()) %> </h4>
</div>
    <form action="/user_check_page.jsp" method="post">
        <p align="center"><input type="submit" value="вернуться на главную"></p>
    </form>

</body>
</html>