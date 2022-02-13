package org.example.webapp.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/database_query")
public class DatabaseServlet extends HttpServlet {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/expenses_management";
    static final String DATABASE_USER = "postgres";
    static final String DATABASE_PASSWORD = "12345";


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        String category_name = request.getParameter("select");
        String datefrom = request.getParameter("datefrom");
        String dateto = request.getParameter("dateto");


        String querytodatabase = "select expense.name, expense.created_at, expense.amount, category.name from expense, category " +
                "where (category.id = expense.category) and ";
        if (!category_name.equals("") && !category_name.equals("Category")) querytodatabase += "(category.name like '" + category_name +"') ";
        else querytodatabase += "(category.name like '%') ";
        if (!datefrom.equals("")) querytodatabase += "and (expense.created_at >= '" + datefrom +"') ";
        if (!dateto.equals("")) querytodatabase += "and (expense.created_at <= '" + dateto +"') ";
        querytodatabase += "order by created_at;";

        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(querytodatabase);

            response.setContentType("text/html");
            String title = "Expenses_management";
            String docType = "<!DOCTYPE html>";
            writer.println(docType + "<html><head><title>" + title + "</title>");
            writer.println("<style>" +
                    ".table {\n" +
                    "    width: 40%;\n" +
                    "    border: none;\n" +
                    "    margin-bottom: 20px;\n" +
                    "}\n" +
                    ".table thead th {\n" +
                    "    font-weight: bold;\n" +
                    "    text-align: left;\n" +
                    "    border: none;\n" +
                    "    padding: 10px 15px;\n" +
                    "    background: #d8d8d8;\n" +
                    "    font-size: 14px;\n" +
                    "    border-left: 1px solid #ddd;\n" +
                    "    border-right: 1px solid #ddd;\n" +
                    "}\n" +
                    ".table tbody td {\n" +
                    "    text-align: left;\n" +
                    "    border-left: 1px solid #ddd;\n" +
                    "    border-right: 1px solid #ddd;\n" +
                    "    padding: 10px 15px;\n" +
                    "    font-size: 14px;\n" +
                    "    vertical-align: top;\n" +
                    "}\n" +
                    ".table thead tr th:first-child, .table tbody tr td:first-child {\n" +
                    "    border-left: none;\n" +
                    "}\n" +
                    ".table thead tr th:last-child, .table tbody tr td:last-child {\n" +
                    "    border-right: none;\n" +
                    "}\n" +
                    ".table tbody tr:nth-child(even){\n" +
                    "    background: #f3f3f3;\n" +
                    "}" +
                    "</style>");

            writer.println("</head><body>");
            writer.println("<h1>Welcome to expense_management</h1>\n" +
                    "<form action=\"database_query\" method=\"get\" name=\"Find_ex\">\n" +
                    "    <select name=\"select\">\n" +
                    "        <option>Category</option>\n" +
                    "        <option name=\"No_category\">No category</option>\n" +
                    "        <option name=\"Food\">Food</option>\n" +
                    "        <option name=\"Clothes\">Clothes</option>\n" +
                    "        <option name=\"Fasilities\">Fasilities</option>\n" +
                    "        <option name=\"Car\">Car</option>\n" +
                    "        <option name=\"Debts\">Debts</option>\n" +
                    "        <option name=\"Travel\">Travel</option>\n" +
                    "    </select>\n" +
                    "    <input type=\"date\" name=\"datefrom\">\n" +
                    "    <input type=\"date\" name=\"dateto\">\n" +
                    "    <input type=\"submit\" value=\"Search\">\n" +
                    "</form>\n");
            writer.println("<br>");
            writer.println("<table class=\"table\">");
            writer.println("<thead><tr>");
            writer.println("<th>Date</th>");
            writer.println("<th>Expense</th>");
            writer.println("<th>Amount</th>");
            writer.println("<th>Category</th>");
            writer.println("</tr>");
            writer.println("</thead>");
            writer.println("<tbody>");
            boolean isempty = true;
            double sum = 0;
            String month = "";
            HashMap<String, Double> month_sum = new HashMap<>();
            HashMap<String, Double> category_statistics = new HashMap<>();
            while (resultSet.next()) {
                isempty = false;
                String name = resultSet.getString(1);
                Date created_at = resultSet.getDate(2);

                double amount = resultSet.getDouble(3);
                sum += amount;
                String category = resultSet.getString(4);
                if (category_statistics.containsKey(category)) category_statistics.put(category, category_statistics.get(category) + amount);
                else
                    category_statistics.put(category, amount);
                month = created_at.toString();
                month = month.substring(5,7);
                if (month_sum.containsKey(month)) month_sum.put(month, month_sum.get(month) + amount);
                else
                    month_sum.put(month, amount);
                writer.println("<tr>");
                writer.println("<td>" + created_at + "</td>");
                writer.println("<td>" + name + "</td>");
                writer.println("<td>" + amount + " </td>");
                writer.println("<td>" + category + "</td>");
                writer.println("</tr>");
            }
            writer.println("</tbody>");
            writer.println("</table>");

            if (!isempty) {
                writer.println("<table class=\"table\">");
                writer.println("<thead><tr>");
                writer.println("<th colspan=\"2\">Statistics:</th>");
                writer.println("</tr>");
                writer.println("</thead>");
                writer.println("<tbody>");
                writer.println("<tr>");
                writer.println("<td>Total amounts:</td><td>" + String.format("%.2f %n", sum) + "</td>");
                writer.println("</tr>");
                writer.println("<tr>");
                writer.println("<th align = \"left\" colspan=\"2\">Categories with the highest spending:</th>");
                writer.println("</tr>");

                String maxcat = "";
                double max = 0;
                while (!category_statistics.isEmpty()) {
                    for (Map.Entry<String, Double> entry : category_statistics.entrySet()) {
                        if (entry.getValue() > max) {
                            max = entry.getValue();
                            maxcat = entry.getKey();
                        }
                    }
                    writer.println("<tr>");
                    writer.println("<td>" + maxcat + "</td><td>" + String.format("%.2f %n", max) + "</td>");
                    writer.println("</tr>");
                    category_statistics.remove(maxcat);
                    maxcat = "";
                    max = 0;
                }

                writer.println("<tr>");
                writer.println("<th  align = \"left\" colspan=\"2\">Month with the highest spending:</th>");
                writer.println("</tr>");

                month = "";
                max = 0;
                for (Map.Entry<String, Double> entry : month_sum.entrySet()) {
                    if (max < entry.getValue()) {
                        max = entry.getValue();
                        month = entry.getKey();
                    }
                }

                switch (month) {
                    case "01": month = "January"; break;
                    case "02": month = "February"; break;
                    case "03": month = "March"; break;
                    case "04": month = "April"; break;
                    case "05": month = "May"; break;
                    case "06": month = "June"; break;
                    case "07": month = "July"; break;
                    case "08": month = "August"; break;
                    case "09": month = "September"; break;
                    case "10": month = "October"; break;
                    case "11": month = "November"; break;
                    case "12": month = "December"; break;
                }
                writer.println("<tr>");
                writer.println("<td>" + month + "</td><td>amounts: " + String.format("%.2f %n", max) + "</td>");
                writer.println("</tr>");
                writer.println("</tbody>");
                writer.println("</table>");
            }
            else writer.println("<h2>Found no results...</h2>");


            resultSet.close();
            statement.close();

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        writer.println("</body></html>");
    }


}
