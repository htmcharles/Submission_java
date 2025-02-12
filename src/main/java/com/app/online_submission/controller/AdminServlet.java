package com.app.online_submission.controller;

import com.app.online_submission.model.*;
import com.app.online_submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AdminServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        // Retrieve all assignments
        List<Assignment> allAssignments;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allAssignments = hibernateSession.createQuery("FROM Assignment", Assignment.class).list();
        }

        // Retrieve all submissions
        List<Submission> allSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allSubmissions = hibernateSession.createQuery("FROM Submission", Submission.class).list();
        }

        // Retrieve all courses
        List<Course> allCourses;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allCourses = hibernateSession.createQuery("FROM Course", Course.class).list();
        }

        // Pass data to the JSP
        request.setAttribute("assignments", allAssignments);
        request.setAttribute("submissions", allSubmissions);
        request.setAttribute("courses", allCourses);
        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String roleString = request.getParameter("role");  // Assuming the role is passed as a parameter

        // Ensure roleString is not null and matches the enum names
        Role role = null;
        try {
            role = Role.valueOf(roleString); // Converts the roleString to Role enum
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid role.");
            request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
            return;
        }

        // Validate input
        if (username == null || password == null || role == null ||
                (!role.equals(Role.STUDENT) && !role.equals(Role.TEACHER))) {
            request.setAttribute("errorMessage", "Invalid input");
            request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
            return;
        }

        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = hibernateSession.beginTransaction();

            // Check if username already exists
            List<User> existingUsers = hibernateSession.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .list();
            if (!existingUsers.isEmpty()) {
                request.setAttribute("errorMessage", "Username already exists.");
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                return;
            }

            // Insert new user
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole(role);

            hibernateSession.persist(newUser);
            tx.commit();

            request.setAttribute("successMessage", "User added successfully.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error adding user.");
        }

        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

}