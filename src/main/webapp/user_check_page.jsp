<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>


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
    <h3 align="center">Добро пожаловать в онлайн-магазин!</h3>
<form action="/order_page.jsp" method="post">

<p align="center">
<label for="username">введите имя пользователя: <input type="text" id="username" required name="username"></label>
</p>

<p align="center">
<label>введите Ваш пароль: <input type="text" name="password"></label>
</p>
            <p align="center"><input type="checkbox" name="check" > я согласен с условиями пользовательского соглашения</p>
<p align="center"><input type="submit" value="перейти в магазин"></p>

</form>
</body>
</html>