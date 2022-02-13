
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Expenses management</title>
</head>
<body>
<h1>Welcome to expense_management</h1>
<form action="database_query" method="get" name="Find_ex">
    <select name="select">
        <option>Category</option>
        <option name="No_category">No category</option>
        <option name="Food">Food</option>
        <option name="Clothes">Clothes</option>
        <option name="Fasilities">Fasilities</option>
        <option name="Car">Car</option>
        <option name="Debts">Debts</option>
        <option name="Travel">Travel</option>
    </select>
    <input type="date" name="datefrom">
    <input type="date" name="dateto">
    <input type="submit" value="Search">
</form>
</body>
</html>
