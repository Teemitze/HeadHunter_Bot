package repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RepositoryVacancy {

    private static final Logger log = LoggerFactory.getLogger(RepositoryVacancy.class);

    Connection connection;

    public RepositoryVacancy(Connection connection) {
        this.connection = connection;
    }

    public void addVacancy(Set<String> employeesLink) {
        try {
            final Date today = new Date(System.currentTimeMillis());
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO employees (employeeLink, inviteDate) VALUES (?, ?)");
            for (String employeeLink : employeesLink) {
                statement.setString(1, employeeLink);
                statement.setDate(2, today);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error("addVacancy error!", e);
        }
    }

    public List<String> findUniqueVacancy(List<String> employeesLink) {
        final List<String> duplicateEmployees = new ArrayList<>();
        try {

            String selectEmployeesLink = "SELECT employeeLink FROM employees WHERE employeeLink IN(";

            StringBuilder stringBuilder = new StringBuilder(selectEmployeesLink);

            for (int i = 0; i < employeesLink.size(); i++) {
                if (i == employeesLink.size() - 1) {
                    stringBuilder.append("?)");
                } else {
                    stringBuilder.append("?,");
                }
            }

            selectEmployeesLink = stringBuilder.toString();
            PreparedStatement statement = connection.prepareStatement(selectEmployeesLink);

            for (String employeeLink : employeesLink) {
                statement.setString(employeesLink.indexOf(employeeLink) + 1, employeeLink);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                duplicateEmployees.add(resultSet.getString("employeeLink"));
            }

        } catch (SQLException e) {
            log.error("findUniqueVacancy error!", e);
            throw new RuntimeException(e);
        }

        employeesLink.removeAll(duplicateEmployees);
        return employeesLink;
    }

    public Long countOfferDay() {
        try {
            final Statement statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery("SELECT count(id) FROM employees WHERE DATE(inviteDate) = DATE(NOW())");
            resultSet.next();
            return resultSet.getLong("count(id)");
        } catch (SQLException e) {
            log.error("countOfferDay error!", e);
            throw new RuntimeException(e);
        }
    }
}
