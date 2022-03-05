package attendance.attendance.service;

import attendance.attendance.model.CanvasConfig;
import attendance.attendance.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import utils.LinkHeaderParser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class StudentService {

    private ObjectMapper mapper;

    public StudentService() {
        mapper = new ObjectMapper();
    }

    public List<Student> getStudents(CanvasConfig config, String token) {
        Map<Integer, Student> studentMap = new HashMap<>();
        String studentIdsURL = buildGradeBookColumnsURL(config, config.getStudentIdCol());
        addStudentIds(studentIdsURL, token, studentMap);
        String studentLabsURL = buildGradeBookColumnsURL(config, config.getLabSectionCol());
        addStudentLabs(studentLabsURL, token, studentMap);
        return new ArrayList<>(studentMap.values());
    }

    private String buildGradeBookColumnsURL(CanvasConfig config, int columnId) {
        String baseURL = config.getBaseUrl() + config.getCourseCode() + "/custom_gradebook_columns/" + columnId + "/data";
        return UriComponentsBuilder.fromUriString(baseURL).queryParam("per_page", 100).build().toUriString();
    }

    private void addStudentIds(String url, String token, Map<Integer, Student> studentMap) {
        ResponseEntity<String> resp = getRequestStudents(url, token);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Student[] students = mapper.readValue(resp.getBody(), Student[].class);
            for (Student student: students) {
                studentMap.put(student.getCanvasId(), student);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<String> linkHeaders = getLinkHeader(resp);
        String nextLink = getNextLink(linkHeaders);

        if (LinkHeaderParser.LAST_LINK.equals(nextLink)) {
            return;
        }

        addStudentIds(nextLink, token, studentMap);
    }

    private void addStudentLabs(String url, String token, Map<Integer, Student> studentMap) {
        ResponseEntity<String> resp = getRequestStudents(url, token);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<Map<String, Object>> mapped = mapper.readValue(resp.getBody(), new TypeReference<ArrayList<Map<String, Object>>>(){});
            for (Map<String, Object> student: mapped) {
                Integer studentCanvasId = (Integer) student.get("user_id");
                if (studentMap.containsKey(studentCanvasId)) {
                    Student s = studentMap.get(studentCanvasId);
                    s.setLab((String) student.get("content"));
                    studentMap.put(studentCanvasId, s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<String> linkHeaders = getLinkHeader(resp);
        String nextLink = getNextLink(linkHeaders);

        if (LinkHeaderParser.LAST_LINK.equals(nextLink)) {
            return;
        }
        addStudentLabs(nextLink, token, studentMap);
    }

    private ResponseEntity<String> getRequestStudents(String url, String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    private List<String> getLinkHeader(ResponseEntity<String> resp) {
        return resp.getHeaders().get("Link");
    }

    private String getNextLink(List<String> linkHeaders) {
        if (linkHeaders == null || linkHeaders.isEmpty())
            return "";

        String links = URLDecoder.decode(linkHeaders.get(0), StandardCharsets.UTF_8);
        Map<String, String> linkMap = LinkHeaderParser.parseLinkHeader(links);
        return LinkHeaderParser.getNext(linkMap);
    }

//    private String getStudentsHelper(String url, List<Student> students) {
//        ResponseEntity<String> resp = getStudents(url);
//        List<String> linkHeader = resp.getHeaders().get("Link");
//
//        try {
//            students.addAll(mapper.readValue(resp.getBody(), new TypeReference<List<Student>>(){}));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        if (linkHeader != null && !linkHeader.isEmpty()) {
//            String links = URLDecoder.decode(linkHeader.get(0), StandardCharsets.UTF_8);
//            Map<String, String> mapped = LinkHeaderParser.parseLinkHeader(links);
//            return LinkHeaderParser.getNext(mapped);
//        } else {
//            return "";
//        }
//    }
}
