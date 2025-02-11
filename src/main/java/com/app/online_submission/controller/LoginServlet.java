package com.app.online_submission.controller;

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

            // Check the user's role and redirect to the appropriate page
            if ("student".equals(user.getRole())) {
                // Redirect to student-specific page (e.g., home.jsp for students)
                response.sendRedirect("student_home.jsp");
            } else if ("teacher".equals(user.getRole())) {
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
