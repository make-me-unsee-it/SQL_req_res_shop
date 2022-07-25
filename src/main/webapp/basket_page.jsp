<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.step.hryshkin.model.User"%>
<%@ page import="com.step.hryshkin.utils.UtilsForOnlineShop"%>

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

<div align="center">
    <h4>Ваш заказ:</h4>
         <ol style="display: table; margin:0 auto;">
            <c:forEach items="${UtilsForOnlineShop.printGoodsForCurrentUser(user.getLogin())}" var="item" >
                <li>${item}</li>
            </c:forEach>
         </ol>
    <h4>Сумма: <%= UtilsForOnlineShop.printTotalPriceForCurrentUser(((User) request.getSession().getAttribute("user")).getLogin()) %> </h4>
</div>
    <form action="/user_check_page.jsp" method="post">
        <p align="center"><input type="submit" value="вернуться на главную"></p>
    </form>

</body>
</html>