package attendance.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {
    @Id
    @JsonProperty("sis_user_id")
    private int studentId;

    @JsonProperty("id")
    private int canvasId;

    @OneToOne
    @JoinColumn(name = "name")
    private Lab lab;

    @Override
    public String toString() {
        return "Student ID: " + studentId + " Canvas ID: " + canvasId;
    }
}
