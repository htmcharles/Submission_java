package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Course;
import com.app.online_submission.model.Submission;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import org.hibernate.Session;

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
}
