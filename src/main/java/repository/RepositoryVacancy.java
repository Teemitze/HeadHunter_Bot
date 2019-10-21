package repository;

import org.hibernate.Session;
import entity.Employee;
import org.hibernate.query.Query;

import java.util.Date;

public class RepositoryVacancy {

    public void addVacancy(String employeeLink) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Employee employee = new Employee(employeeLink, new Date());
        session.save(employee);
        session.flush();
        session.getTransaction().commit();
        session.close();
    }

    public Employee findVacancy(String employeeLink) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Employee employee = session.createQuery("from Employee where employeeLink=:employeeLink", Employee.class).setParameter("employeeLink", employeeLink).uniqueResult();
        session.close();
        return employee;
    }

    public static long countOfferDay() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        long countEmployee = (long) session.createQuery("SELECT count(*) FROM Employee WHERE DATE(inviteDate) = DATE(NOW())").getSingleResult();
        return countEmployee;
    }
}
