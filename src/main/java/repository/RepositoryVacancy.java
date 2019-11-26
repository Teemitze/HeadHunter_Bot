package repository;

import entity.Employee;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

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

    public List<String> findVacancy(List<String> employeesLink) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<String> duplicateEmployees = session.createQuery("select employeeLink from Employee where employeeLink IN :employeesLink").setParameterList("employeesLink", employeesLink).getResultList();
        employeesLink.removeAll(duplicateEmployees);
        session.close();
        return employeesLink;
    }

    public static Long countOfferDay() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (Long) session.createQuery("SELECT count(id) FROM Employee WHERE DATE(inviteDate) = DATE(NOW())").getSingleResult();
    }
}
