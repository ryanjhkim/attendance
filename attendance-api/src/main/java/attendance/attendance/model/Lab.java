package attendance.attendance.model;

import javax.persistence.*;

@Entity
@Table
public class Lab {
    @Id
    String name;
    int canvasId;
    @ManyToOne
    @JoinColumn(name = "id")
    LabSection section;
}
