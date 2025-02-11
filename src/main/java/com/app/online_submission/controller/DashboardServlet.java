package com.app.online_submission.controller;

import com.app.online_submission.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class DashboardServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }
        // Get the user object from the session and ensure it's casted to User type
        User user = (User) session.getAttribute("user"); // Make sure it's properly casted to User
        request.setAttribute("user", user);  // Add the user object to the request scope
        request.getRequestDispatcher("WEB-INF/dashboard.jsp").forward(request, response);  // Forward to the dashboard JSP
    }
}
