package attendance.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {
    @Id
    @JsonProperty("content")
    private int studentId;

    @JsonProperty("user_id")
    private int canvasId;


    private String lab;

    @Override
    public String toString() {
        return "Student ID: " + studentId + " Canvas ID: " + canvasId + "Lab: " + lab;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(int canvasId) {
        this.canvasId = canvasId;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

}
