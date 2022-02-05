package attendance.attendance.model;



public class CanvasConfig {
    private String courseCode;
    private String term;
    private int studentIdCol;
    private int labSectionCol;
    private final String baseUrl = "https://canvas.ubc.ca/api/v1/courses/";

    public String getCourseCode() {
        return courseCode;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getStudentIdCol() {
        return studentIdCol;
    }

    public void setStudentIdCol(int studentIdCol) {
        this.studentIdCol = studentIdCol;
    }

    public int getLabSectionCol() {
        return labSectionCol;
    }

    public void setLabSectionCol(int labSectionCol) {
        this.labSectionCol = labSectionCol;
    }
}
