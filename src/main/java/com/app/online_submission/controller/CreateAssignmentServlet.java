package com.app.online_submission.controller;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Course;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

import com.app.online_submission.model.Role;

@WebServlet("/createAssignment")
public class CreateAssignmentServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/create_assignment.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User instructor = (User) session.getAttribute("user");

        if (instructor != null && instructor.getRole().compareTo(Role.valueOf("TEACHER")) == 0) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            LocalDateTime deadline = LocalDateTime.parse(request.getParameter("deadline"));
            int courseId = Integer.parseInt(request.getParameter("courseId"));

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session hibernateSession = sessionFactory.openSession();
            hibernateSession.beginTransaction();

            Course course = hibernateSession.get(Course.class, courseId);
            Assignment assignment = new Assignment();
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);
            assignment.setCourse(course);
            assignment.setInstructor(instructor);

            hibernateSession.persist(assignment);
            hibernateSession.getTransaction().commit();
            hibernateSession.close();

            response.sendRedirect("teacher_dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp?error=unauthorized");
        }
    }
}
