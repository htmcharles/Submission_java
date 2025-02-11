package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.User;
import com.app.online_submission.service.AssignmentService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CreateAssignmentServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }
        User instructor = (User) session.getAttribute("user");
        AssignmentService assignmentService = AssignmentService.getInstance();
        List<Assignment> assignments = assignmentService.getAllAssignmentsByInstructor(instructor);

        // Troubleshooting: Log the assignment list size or null check in the terminal
        if (assignments == null) {
            System.out.println("Assignments list is null.");
        } else if (assignments.isEmpty()) {
            System.out.println("Assignments list is empty.");
        } else {
            System.out.println("Assignments found: " + assignments.size());
        }

        // Pass assignments to the JSP
        request.setAttribute("assignments", assignments);
        request.getRequestDispatcher("teacher_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the session and ensure the user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        // Get form data
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String deadlineStr = request.getParameter("deadline");

        // Validate deadline
        LocalDateTime deadline = LocalDateTime.parse(deadlineStr);

        // Get the logged-in user (instructor)
        User instructor = (User) session.getAttribute("user");

        // Create a new assignment object
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(deadline);
        assignment.setInstructor(instructor);

        // Call the AssignmentService to save the assignment
        AssignmentService assignmentService = AssignmentService.getInstance();
        assignmentService.addAssignment(assignment);

        // Redirect to teacher dashboard with success message
        response.sendRedirect("teacher_dashboard.jsp?message=Assignment%20created%20successfully");
    }
}
