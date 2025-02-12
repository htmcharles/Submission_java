package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Submission;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;

public class StudentServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        // Get the logged-in student
        User student = (User) session.getAttribute("user");

        // Retrieve all assignments for display
        List<Assignment> allAssignments;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            allAssignments = hibernateSession.createQuery("FROM Assignment", Assignment.class).list();
        }

        // Troubleshooting: Log the assignment list size or null check
        if (allAssignments == null) {
            System.out.println("All assignments list is null.");
        } else if (allAssignments.isEmpty()) {
            System.out.println("All assignments list is empty.");
        } else {
            System.out.println("All assignments found: " + allAssignments.size());
        }

        // Fetch student-specific submissions
        List<Submission> studentSubmissions;
        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            studentSubmissions = hibernateSession.createQuery("FROM Submission WHERE student = :student", Submission.class)
                    .setParameter("student", student)
                    .list();
        }

        // Troubleshooting: Log the submission list size or null check
        if (studentSubmissions == null) {
            System.out.println("Student submissions list is null.");
        } else if (studentSubmissions.isEmpty()) {
            System.out.println("Student submissions list is empty.");
        } else {
            System.out.println("Student submissions found: " + studentSubmissions.size());
        }

        // Pass all assignments and student submissions to the JSP
        request.setAttribute("assignments", allAssignments);
        request.setAttribute("submissions", studentSubmissions);
        request.getRequestDispatcher("student_submissions.jsp").forward(request, response);
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
        String assignmentId = request.getParameter("assignment");
        String studentId = request.getParameter("studentId");

        // Handle file upload
        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);

        // Perform necessary checks (e.g., validate file type and size, etc.)
        if (assignmentId != null && studentId != null && filePart != null) {
            // Save the file on the server
            String filePath = saveFile(filePart, fileName);

            // Save the submission record to the database
            try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                hibernateSession.beginTransaction();

                Assignment assignment = hibernateSession.get(Assignment.class, Long.parseLong(assignmentId));
                User student = (User) session.getAttribute("user");

                // Create a new submission and set the properties
                Submission submission = new Submission();
                submission.setAssignment(assignment);
                submission.setStudent(student);
                submission.setFilePath(filePath);
                submission.setSubmissionTime(LocalDateTime.now());

                // Save submission
                hibernateSession.persist(submission);
                hibernateSession.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("StudentServlet?error=submission_failed");
                return;
            }

            // Redirect back to the student home with success message
            response.sendRedirect("StudentServlet?success=submission_successful");
        } else {
            response.sendRedirect("StudentServlet?error=missing_fields");
        }
    }

    // Utility method to get the file name from the Part object
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("Content-Disposition");
        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf("=") + 2, cd.length() - 1);
            }
        }
        return null;
    }

    // Utility method to save the file to a specific path on the server
    private String saveFile(Part filePart, String fileName) throws IOException {
        String uploadDir = getServletContext().getRealPath("/uploads");
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        File file = new File(uploadDir + File.separator + fileName);
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return file.getAbsolutePath();
    }
}
