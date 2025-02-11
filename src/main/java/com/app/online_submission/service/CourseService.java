package com.app.online_submission.service;

import com.app.online_submission.model.Course;
import com.app.online_submission.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CourseService {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Course getCourseById(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Course.class, courseId); // Fetch course by ID
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching course", e);
        }
    }
}
