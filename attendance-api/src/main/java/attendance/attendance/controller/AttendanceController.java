package attendance.attendance.controller;

import attendance.attendance.model.Attendance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

    @PostMapping(path="attend", consumes= MediaType.APPLICATION_JSON_VALUE)
    public void postAttendance(@RequestBody String attendance) {
        ObjectMapper mapper = new ObjectMapper();
        Attendance attendance1 = null;
        try {
            attendance1 = mapper.readValue(attendance, Attendance.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Attendance testAttendance = new Attendance();


        System.out.println(attendance);
        System.out.println(attendance1);
    }
}
