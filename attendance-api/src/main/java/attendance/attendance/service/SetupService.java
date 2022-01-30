package attendance.attendance.service;

import attendance.attendance.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import utils.LinkHeaderParser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Service
public class SetupService {
    
    private final String BASE_URL = "https://canvas.ubc.ca/api/v1/courses";
    private final int COURSE_CODE = 78025;
    private final String token = "PLACEHOLDER";
    private ObjectMapper mapper;

    public SetupService() {
        mapper = new ObjectMapper();
    }

    @GetMapping("/get/students")
    public void getStudents() {
        String url = BASE_URL + "/" + COURSE_CODE + "/users?per_page=100";
        List<Student> students = new ArrayList();

        while (true) {
            url = getStudentsHelper(url, students);
            if ("".equals(url)) break;
        }
        System.out.println(students);
    }

    private String getStudentsHelper(String url, List<Student> students) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
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
