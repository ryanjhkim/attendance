package attendance.attendance.model;

import javax.persistence.*;

@Entity
@Table
public class Student {
    @Id
    private int studentId;
    private int canvasId;

    @OneToOne
    @JoinColumn(name = "name")
    private Lab lab;
}
