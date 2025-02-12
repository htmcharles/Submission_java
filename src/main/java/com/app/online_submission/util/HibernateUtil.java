package com.app.online_submission.util;

import com.app.online_submission.model.User;
import com.app.online_submission.model.Course;
import com.app.online_submission.model.Assignment;
import com.app.online_submission.model.Submission;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;



import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();

            // MySQL Configuration
            settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            settings.put(Environment.URL, "jdbc:mysql://localhost:3306/online_submission");  // Use the online_submission DB
            settings.put(Environment.USER, "root");
            settings.put(Environment.PASS, "");  // Adjust the password if needed
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

            // Show SQL in console
            settings.put(Environment.SHOW_SQL, true);

            // Auto-create/drop schema
            settings.put(Environment.HBM2DDL_AUTO, "update");  // 'update' to update schema automatically

            // Apply settings to configuration
            configuration.setProperties(settings);

            // Add annotated entity classes
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Course.class);
            configuration.addAnnotatedClass(Assignment.class);
            configuration.addAnnotatedClass(Submission.class);

            // Create ServiceRegistry
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            // Build SessionFactory
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
}
