package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Course;
import com.app.online_submission.model.Submission;
import com.app.online_submission.model.User;
import com.app.online_submission.service.AssignmentService;
import com.app.online_submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;

public class TeacherServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        User instructor = (User) session.getAttribute("user");
        AssignmentService assignmentService = AssignmentService.getInstance();
        List<Assignment> assignments = assignmentService.getAllAssignmentsByInstructor(instructor);

        // Fetch all submissions from the database
        List<Submission> allSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allSubmissions = hibernateSession.createQuery("FROM Submission", Submission.class).list();
        }

        // Troubleshooting: Log the submission list size or null check
        if (allSubmissions == null) {
            System.out.println("All submissions list is null.");
        } else if (allSubmissions.isEmpty()) {
            System.out.println("All submissions list is empty.");
        } else {
            System.out.println("All submissions found: " + allSubmissions.size());
        }

        // Fetch all courses from the database
        List<Course> courses;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            courses = hibernateSession.createQuery("FROM Course", Course.class).list();
        }

        // Pass assignments, courses, and submissions to the JSP
        request.setAttribute("assignments", assignments);
        request.setAttribute("courses", courses);
        request.setAttribute("submissions", allSubmissions);
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
            response.sendRedirect("TeacherServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeacherServlet");
        }
    }
}
