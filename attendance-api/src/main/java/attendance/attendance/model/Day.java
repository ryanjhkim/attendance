package attendance.attendance.model;

public enum Day {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    private int day;

    Day(int day) {
        this.day = day;
    }
}
