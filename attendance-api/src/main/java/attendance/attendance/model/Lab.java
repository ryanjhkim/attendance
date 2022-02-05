package attendance.attendance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lab {

    @Id
    String name;
    int canvasId;

    /* TODO figure out how to sync lab sections...
    @ManyToOne
    @JoinColumn(name = "id")
    LabSection section;
    */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCanvasId() {
        return canvasId;
    }

    public void setCanvasId(int canvasId) {
        this.canvasId = canvasId;
    }


    @Override
    public String toString() {
        return name;
    }
}
