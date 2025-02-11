package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Submission;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.app.online_submission.model.Role;


@WebServlet("/uploadAssignment")
public class SubmitAssignmentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User student = (User) session.getAttribute("user");

        if (student != null && student.getRole().compareTo(Role.valueOf("STUDENT")) == 0) {
            int assignmentId = Integer.parseInt(request.getParameter("assignmentId"));
            Part filePart = request.getPart("file");
            String fileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
            String filePath = "uploads/" + fileName;  // Path to save files

            // Save the file to the server
            InputStream fileContent = filePart.getInputStream();
            Path filePathToSave = Paths.get(getServletContext().getRealPath("/" + filePath));
            Files.copy(fileContent, filePathToSave);

            // Create the submission
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session hibernateSession = sessionFactory.openSession();
            hibernateSession.beginTransaction();

            Assignment assignment = hibernateSession.get(Assignment.class, assignmentId);
            Submission submission = new Submission();
            submission.setAssignment(assignment);
            submission.setStudent(student);
            submission.setFilePath(filePath);
            submission.setSubmissionTime(java.time.LocalDateTime.now().toString());

            hibernateSession.persist(submission);
            hibernateSession.getTransaction().commit();
            hibernateSession.close();

            response.sendRedirect("student_home.jsp");
        } else {
            response.sendRedirect("login.jsp?error=unauthorized");
        }
    }
}
