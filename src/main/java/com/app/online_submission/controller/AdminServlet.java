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
        String courseName = request.getParameter("courseName");
        String courseDescription = request.getParameter("courseDescription");
        String instructorIdString = request.getParameter("instructorId");

        if (courseName == null || courseDescription == null || instructorIdString == null) {
            request.setAttribute("errorMessage", "Invalid input.");
            request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
            return;
        }

        try {
            // Parse the instructor ID as an integer
            int instructorId = Integer.parseInt(instructorIdString);

            // Check if the instructor exists and is valid
            User instructor = getInstructorById(instructorId);
            if (instructor == null || !instructor.getRole().equals(Role.TEACHER)) {
                request.setAttribute("errorMessage", "Invalid instructor.");
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
                return;
            }

            // Create a new course and set the instructorId
            Course newCourse = new Course();
            newCourse.setName(courseName);
            newCourse.setDescription(courseDescription);
            newCourse.setInstructorId(instructorId); // Set instructor_id directly

            // Persist the new course in the database
            try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = hibernateSession.beginTransaction();
                hibernateSession.persist(newCourse);
                tx.commit();
                request.setAttribute("successMessage", "Course added successfully.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Instructor ID must be a valid number.");
        }

        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }

    private User getInstructorById(int instructorId) {
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            return hibernateSession.get(User.class, instructorId); // Fetch the user by instructor ID
        }
    }

}
