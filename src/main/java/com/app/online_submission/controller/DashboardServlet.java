package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.service.AssignmentService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class DashboardServlet extends HttpServlet {

    private final AssignmentService assignmentService = AssignmentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the session and ensure the user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        try {
            // Get instructor's username from session
            String instructor = session.getAttribute("user").toString();
            System.out.println("Instructor username: " + instructor); // Debugging line

            // Fetch assignments
            List<Assignment> assignments = assignmentService.getAllAssignmentsByInstructor(instructor);
            System.out.println("Assignments fetched: " + assignments.size()); // Debugging line
            request.setAttribute("assignments", assignments);

            // Forward to JSP
            request.getRequestDispatcher("teacher_dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Error%20loading%20assignments");
        }
    }
}
