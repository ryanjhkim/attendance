package attendance.attendance.service;

import attendance.attendance.db.LabRepository;
import attendance.attendance.db.StudentRepository;
import attendance.attendance.model.CanvasConfig;
import attendance.attendance.model.LabSection;
import attendance.attendance.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.List;

@Service
public class SetupService {
    private ObjectMapper mapper;
    private final StudentRepository studentRepository;
    private final LabRepository labRepository;
    private final LabService labService;
    private final StudentService studentService;

    private final String STUDENT_ID = "Student Number";
    private final String LAB = "Lab";

    @Autowired
    public SetupService(LabRepository labRepository, StudentRepository studentRepository, LabService labService, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.labService = labService;
        this.studentService = studentService;
        this.labRepository = labRepository;
        mapper = new ObjectMapper();
    }

    public void setup(Map<String, String> headers, String courseCode, String term) {
        String token = headers.get("authorization").replaceFirst("^Bearer ", "");
        CanvasConfig config = new CanvasConfig();
        config.setCourseCode(courseCode);
        config.setTerm(term);
        Map<String, Integer> customGradeBookColumns = getCustomGradeBookColumnIds(config, token);
        config.setStudentIdCol(customGradeBookColumns.get(STUDENT_ID));
        config.setLabSectionCol(customGradeBookColumns.get(LAB));
        List<LabSection> sections = labService.getLabs(config.getTerm());
        List<Student> students = studentService.getStudents(config, token);
        
        labRepository.saveAll(sections);
        studentRepository.saveAll(students);
    }

    private Map<String, Integer> getCustomGradeBookColumnIds(CanvasConfig config, String token) {
        Map<String, Integer> columnsToIds = new HashMap<>();
        String url = config.getBaseUrl() + config.getCourseCode() + "/custom_gradebook_columns";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            ArrayList<LinkedHashMap<String, Object>> res =  mapper.readValue(resp.getBody(), ArrayList.class);
            for (LinkedHashMap<String, Object> jsonObj : res) {
                if (STUDENT_ID.equals(jsonObj.get("title"))) {
                    columnsToIds.put(STUDENT_ID, (Integer) jsonObj.get("id"));
                }

                if (LAB.equals(jsonObj.get("title"))) {
                    columnsToIds.put(LAB, (Integer) jsonObj.get("id"));
                }
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return columnsToIds;
    }
}
