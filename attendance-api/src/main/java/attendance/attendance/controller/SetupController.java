package attendance.attendance.controller;

import attendance.attendance.service.SetupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SetupController {

    private final SetupService setupService;

    public SetupController(SetupService setupService) {
        this.setupService = setupService;
    }

    @GetMapping("/setup")
    public void setup(@RequestHeader Map<String, String> headers,
                      @RequestParam(name = "course_code") String courseCode,
                      @RequestParam(name="term") String term) {
        this.setupService.setup(headers, courseCode, term);
    }

}
