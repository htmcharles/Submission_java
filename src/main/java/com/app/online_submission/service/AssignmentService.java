package com.app.online_submission.service;

import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.User;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class AssignmentService {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static AssignmentService instance;

    // Singleton pattern
    public static synchronized AssignmentService getInstance() {
        if (instance == null) {
            instance = new AssignmentService();
        }
        return instance;
    }

    public void addAssignment(Assignment assignment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(assignment);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving assignment", e);
        }
    }

    public List<Assignment> getAllAssignmentsByInstructor(User instructor) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Assignment a WHERE a.instructor = :instructor", Assignment.class)
                    .setParameter("instructor", instructor)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching assignments for instructor", e);
        }
    }
}
