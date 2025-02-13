package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Course;
import com.app.online_submission.model.Submission;
import com.app.online_submission.model.User;
import com.app.online_submission.service.AssignmentService;
import com.app.online_submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
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

        String downloadFileId = request.getParameter("download");
        if (downloadFileId != null) {
            downloadFile(Long.parseLong(downloadFileId), response);
            return;
        }

        User instructor = (User) session.getAttribute("user");
        AssignmentService assignmentService = AssignmentService.getInstance();
        List<Assignment> assignments = assignmentService.getAllAssignmentsByInstructor(instructor);

        List<Submission> allSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allSubmissions = hibernateSession.createQuery("FROM Submission", Submission.class).list();

            // Check if each submission is late and set the flag
            for (Submission submission : allSubmissions) {
                if (submission.getSubmissionTime().isAfter(submission.getAssignment().getDeadline())) {
                    submission.setLate(true);
                } else {
                    submission.setLate(false);
                }
            }
        }

        List<Course> courses;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            courses = hibernateSession.createQuery("FROM Course", Course.class).list();
        }

        request.setAttribute("assignments", assignments);
        request.setAttribute("courses", courses);
        request.setAttribute("submissions", allSubmissions);
        request.getRequestDispatcher("teacher_dashboard.jsp").forward(request, response);
    }

    private void downloadFile(Long submissionId, HttpServletResponse response) throws IOException {
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            Submission submission = hibernateSession.get(Submission.class, submissionId);
            if (submission != null) {
                File file = new File(submission.getFilePath());
                if (file.exists()) {
                    response.setContentType(Files.probeContentType(file.toPath()));
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        fileInputStream.transferTo(response.getOutputStream());
                    }
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        LocalDateTime deadline = LocalDateTime.parse(request.getParameter("deadline"));
        User instructor = (User) session.getAttribute("user");

        // Validate deadline to ensure it's not in the past
        if (deadline.isBefore(LocalDateTime.now())) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('Late submission not allowed'); window.location='TeacherServlet';</script>");
            return;
        }

        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            hibernateSession.beginTransaction();
            Course course = hibernateSession.get(Course.class, courseId);
            if (course == null) {
                response.sendRedirect("error.jsp?message=Course%20not%20found");
                return;
            }

            Assignment assignment = new Assignment();
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);
            assignment.setInstructor(instructor);
            assignment.setCourse(course);

            hibernateSession.persist(assignment);
            hibernateSession.getTransaction().commit();

            response.sendRedirect("TeacherServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeacherServlet");
        }
    }
}
