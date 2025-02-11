package com.app.online_submission.controller;

import com.app.online_submission.model.Role;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Handle GET request to display login page
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response); // Forward to login.jsp
    }

    // Handle POST request to process login form
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the username and password from the login form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Open Hibernate session and check the user credentials
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("FROM User WHERE username = :username AND password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);

        User user = null;
        try {
            user = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        // If user is found, store the user in the session and redirect to the appropriate page
        if (user != null) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("user", user);

            // Print the user's role for troubleshooting
            System.out.println("Logged in user role: " + user.getRole());

            // Check the user's role and redirect to the appropriate page
            if (user.getRole().compareTo(Role.valueOf("STUDENT")) == 0){
                // Redirect to student-specific page (e.g., student_home.jsp for students)
                response.sendRedirect("student_home.jsp");
            } else if (user.getRole().compareTo(Role.valueOf("TEACHER")) == 0) {
                // Redirect to teacher-specific page (e.g., teacher_dashboard.jsp for teachers)
                response.sendRedirect("teacher_dashboard.jsp");
            } else {
                // In case the role is not recognized
                response.sendRedirect("login.jsp?error=invalid_role");
            }
        } else {
            // If user is not found, redirect to login page with error
            response.sendRedirect("login.jsp?error=invalid_credentials");
        }
    }
}
