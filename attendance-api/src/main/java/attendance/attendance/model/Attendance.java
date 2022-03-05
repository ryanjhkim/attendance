package attendance.attendance.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Attendance {

    @Id @GeneratedValue
    private Long id;
    private Long studentId;
    private Long timestamp;
}
