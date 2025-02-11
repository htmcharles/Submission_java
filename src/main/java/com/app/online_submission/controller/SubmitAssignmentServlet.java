package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.User;
import com.app.online_submission.model.Submission;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/submit_assignment")
public class SubmitAssignmentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User student = (User) session.getAttribute("user");

        if (student != null) {
            // Get assignment and file parameters
            int assignmentId = Integer.parseInt(request.getParameter("assignment_id"));
            Part filePart = request.getPart("file");

            // Generate file path to save it
            String filePath = "uploads/" + student.getId() + "_" + new Timestamp(System.currentTimeMillis()).getTime() + ".pdf";  // Example for PDF

            // Save file on server
            File file = new File(filePath);
            filePart.write(file.getAbsolutePath());

            // Save submission to database
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session hibernateSession = sessionFactory.openSession();
            Transaction transaction = hibernateSession.beginTransaction();

            try {
                // Fetch the assignment from the database
                Assignment assignment = hibernateSession.get(Assignment.class, assignmentId);

                // Create a new Submission object and set values
                Submission submission = new Submission();
                submission.setAssignment(assignment);
                submission.setStudent(student);
                submission.setFilePath(filePath);
                submission.setSubmissionTime(String.valueOf(new Timestamp(System.currentTimeMillis()))); // Set current timestamp

                // Save the submission
                hibernateSession.wait(submission.getId());

                // Commit transaction
                transaction.commit();

                // Redirect to home after successful submission
                response.sendRedirect("home.jsp");

            } catch (Exception e) {
                // Rollback transaction in case of error
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                hibernateSession.close();
            }
        } else {
            // Redirect to login page if user is not authenticated
            response.sendRedirect("login.jsp");
        }
    }
}
