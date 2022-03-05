package attendance.attendance.controller;

import attendance.attendance.model.Attendance;
import attendance.attendance.service.AttendanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping(path="attend", consumes= MediaType.APPLICATION_JSON_VALUE)
    public void postAttendance(@RequestBody Attendance attendance) {
        System.out.println(attendance);
        attendanceService.RecordAttendance(attendance);
    }
}
