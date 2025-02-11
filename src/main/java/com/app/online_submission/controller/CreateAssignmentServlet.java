package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Course;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class CreateAssignmentServlet extends HttpServlet {

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

        // Get the SessionFactory and open a session
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session hibernateSession = sessionFactory.openSession();
        hibernateSession.beginTransaction();

        try {
            // Get the course based on the courseId (this assumes you have a method to fetch the course)
            Course course = hibernateSession.get(Course.class, courseId);

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
            response.sendRedirect("teacher_dashboard.jsp?message=Assignment%20created%20successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Error%20creating%20assignment");
        } finally {
            hibernateSession.close();
        }
    }
}
