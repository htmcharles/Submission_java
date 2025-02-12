package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Course;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;


public class StudentServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }
        List<Assignment> allAssignments;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allAssignments = hibernateSession.createQuery("FROM Assignment", Assignment.class).list();
        }

        // Troubleshooting: Log the assignment list size or null check in the terminal
        if (allAssignments == null) {
            System.out.println("All assignments list is null.");
        } else if (allAssignments.isEmpty()) {
            System.out.println("All assignments list is empty.");
        } else {
            System.out.println("All assignments found: " + allAssignments.size());
        }

        // Pass all assignments to the JSP for student view
        request.setAttribute("assignments", allAssignments);
        request.getRequestDispatcher("student_home.jsp").forward(request, response);
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
        LocalDateTime deadline = LocalDateTime.parse(request.getParameter("deadline"));

        // Get the logged-in user (instructor)
        User instructor = (User) session.getAttribute("user");

        // Log courseId for debugging
        System.out.println("Course ID received: " + courseId);

        // Get the SessionFactory and open a session
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            hibernateSession.beginTransaction();

            // Get the course based on the courseId
            Course course = hibernateSession.get(Course.class, courseId);
            if (course == null) {
                // Log if course is not found
                System.out.println("Course not found with ID: " + courseId);

                // If course does not exist, send an error response
                response.sendRedirect("error.jsp?message=Course%20not%20found");
                return;
            }

            // Log if course was found successfully
            System.out.println("Course found: " + course.getName());

            // Create a new Assignment object
            Assignment assignment = new Assignment();
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);
            assignment.setInstructor(instructor);
            assignment.setCourse(course);

            // Save the assignment to the database
            hibernateSession.persist(assignment);
            hibernateSession.getTransaction().commit();

            // Redirect back to the teacher dashboard with success message
            response.sendRedirect("StudentServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("StudentServlet");
        }
    }

}