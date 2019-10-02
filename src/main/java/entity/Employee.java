package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "employeeLink")
    private String employeeLink;

    @Column(name = "inviteDate")
    private Date inviteDate;

    Employee(){}

    public Employee(String employeeLink, Date inviteDate) {
        this.employeeLink = employeeLink;
        this.inviteDate = inviteDate;
    }
}
