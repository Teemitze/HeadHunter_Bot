package repository;

import configuration.ConfigurationHHBot;
import entity.Employee;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory = buildSessionFactory();

    public static synchronized SessionFactory buildSessionFactory() {
        try {
            Configuration cfg = new Configuration();
            cfg.configure("hibernate.cfg.xml");
            cfg.getProperties().setProperty("hibernate.connection.username", ConfigurationHHBot.LOGIN_DB);
            cfg.getProperties().setProperty("hibernate.connection.password", ConfigurationHHBot.PASSWORD_DB);
            cfg.addAnnotatedClass(Employee.class);
            StandardServiceRegistryBuilder registry = new StandardServiceRegistryBuilder()
                    .applySettings(cfg.getProperties());
            sessionFactory = cfg.buildSessionFactory(registry.build());
        } catch (Exception e) {
            log.error("Initial SessionFactory failed!", e);
            throw new RuntimeException(e);
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}