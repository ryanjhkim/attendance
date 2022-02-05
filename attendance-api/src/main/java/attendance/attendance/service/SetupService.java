package attendance.attendance.service;

import attendance.attendance.db.StudentRepository;
import attendance.attendance.model.Lab;
import attendance.attendance.model.LabSection;
import attendance.attendance.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import utils.LinkHeaderParser;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

@RestController
@Service
public class SetupService {
    
    private final String BASE_URL = "https://canvas.ubc.ca/api/v1/courses";
    private final int COURSE_CODE = 78025;
    private final String token = "PLACEHOLDER";
    private ObjectMapper mapper;
    private final StudentRepository studentRepository;

    @Autowired
    public SetupService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        mapper = new ObjectMapper();
    }

    @GetMapping("/get/students")
    public void getStudents() {
        Map<Integer, Student> studentMap;
        String studentIdsURL = buildGradeBookColumnsURL("89642");
        studentMap = getStudentIds(studentIdsURL);
        String studentLabsURL = buildGradeBookColumnsURL("89644");
        getStudentLabs(studentLabsURL, studentMap);
        List<Student> students = new ArrayList<>(studentMap.values());
        studentRepository.saveAll(students);
    }

    private String buildGradeBookColumnsURL(String columnId) {
        String baseURL = BASE_URL + "/" + COURSE_CODE + "/custom_gradebook_columns/" + columnId + "/data";
        return UriComponentsBuilder.fromUriString(baseURL).queryParam("per_page", 100).build().toUriString();
    }

    private Map<Integer, Student> getStudentIds(String url) {
        ResponseEntity<String> resp = getStudents(url);

        Map<Integer, Student> studentMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Student[] students = mapper.readValue(resp.getBody(), Student[].class);
            for (Student student: students) {
                studentMap.put(student.getCanvasId(), student);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return studentMap;
    }

    private ResponseEntity<String> getStudents(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return resp;
    }

    private Map<Integer, Student> getStudentLabs(String url, Map<Integer, Student> studentMap) {
        ResponseEntity<String> resp = getStudents(url);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<Map<String, Object>> mapped = mapper.readValue(resp.getBody(), new TypeReference<ArrayList<Map<String, Object>>>(){});
            for (Map<String, Object> student: mapped) {
                Integer studentCanvasId = (Integer) student.get("user_id");
                if (studentMap.containsKey(studentCanvasId)) {
                    Student s = studentMap.get(studentCanvasId);
                    Lab lab = new Lab();
                    lab.setName((String) student.get("content"));
                    s.setLab(lab);
                    studentMap.put(studentCanvasId, s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return studentMap;
    }

    private String getStudentsHelper(String url, List<Student> students) {
        ResponseEntity<String> resp = getStudents(url);
        List<String> linkHeader = resp.getHeaders().get("Link");

        try {
            students.addAll(mapper.readValue(resp.getBody(), new TypeReference<List<Student>>(){}));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (linkHeader != null && !linkHeader.isEmpty()) {
            String links = URLDecoder.decode(linkHeader.get(0), StandardCharsets.UTF_8);
            Map<String, String> mapped = LinkHeaderParser.parseLinkHeader(links);
            return LinkHeaderParser.getNext(mapped);
        } else {
            return "";
        }
    }

}
