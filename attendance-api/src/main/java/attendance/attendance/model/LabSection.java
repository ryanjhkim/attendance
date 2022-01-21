package attendance.attendance.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity
public class LabSection {
    @Id
    @SequenceGenerator(
            name = "section_sequence",
            sequenceName = "section_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "section_sequence"
    )
    private Long id;
    Day dayOfWeek;
    LocalDateTime startTime;
    LocalDateTime endTime;
}
