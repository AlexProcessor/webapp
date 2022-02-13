package org.example.webapp.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        PrintWriter out = response.getWriter();
        if (path.equals("/")){
            request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);

            String category = request.getParameter("select");
            out.println(category);

        }
    }
}
