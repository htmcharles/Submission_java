package com.app.online_submission.service;

import com.app.online_submission.model.Submission;
import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class SubmissionService {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static SubmissionService instance;

    // Singleton pattern
    public static synchronized SubmissionService getInstance() {
        if (instance == null) {
            instance = new SubmissionService();
        }
        return instance;
    }

    public void addSubmission(Submission submission) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(submission);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving submission", e);
        }
    }

    public List<Submission> getSubmissionsByStudent(User student) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Submission s WHERE s.student = :student", Submission.class)
                    .setParameter("student", student)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching submissions for student", e);
        }
    }

    public List<Submission> getSubmissionsByAssignment(Assignment assignment) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Submission s WHERE s.assignment = :assignment", Submission.class)
                    .setParameter("assignment", assignment)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching submissions for assignment", e);
        }
    }
}
