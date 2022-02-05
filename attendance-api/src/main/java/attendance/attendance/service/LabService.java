package attendance.attendance.service;

import attendance.attendance.model.LabSection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Service
public class LabService {

    @GetMapping("/get/labs")
    public List<LabSection> getLabs() {
        List<LabSection> sections = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://courses.students.ubc.ca/cs/courseschedule?pname=subjarea&tname=subj-course&dept=CPSC&course=213").get();
            Elements table = doc.select("table.section-summary");
            Elements rows = table.select("tr");

            Map<String, Integer> fieldToIdx = new HashMap<>();
            Element headerRow = rows.get(0);
            Elements headerCols = headerRow.select("th");

            for (int i = 0; i < headerCols.size(); i++) {
                fieldToIdx.put(headerCols.get(i).text(), i);
            }

            String prevSectionName = "";
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() < fieldToIdx.size())
                    continue;

                String activity = cols.get(fieldToIdx.get("Activity")).text();
                String term = cols.get(fieldToIdx.get("Term")).text();

                if ("Laboratory".equals(activity) && "1".equals(term)) {
                   LabSection section = parseLabSection(fieldToIdx, cols, prevSectionName);
                   sections.add(section);
                   prevSectionName = section.getName();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sections;
    }

    private LabSection parseLabSection(Map<String, Integer> fieldToIdx, Elements columns, String labName) {
        LabSection section = new LabSection();
        String dayOfWeek = columns.get(fieldToIdx.get("Days")).text();
        section.setDayOfWeek(DayOfWeek.from(DateTimeFormatter.ofPattern("EEE").parse(dayOfWeek)));
        String startTime = columns.get(fieldToIdx.get("Start Time")).text();
        String endTime = columns.get(fieldToIdx.get("End Time")).text();
        String sectionName = parseLabName(columns.get(fieldToIdx.get("Section")).text());
        sectionName = "".equals(sectionName) ? labName : sectionName;
        section.setStartTime(getTime(startTime));
        section.setEndTime(getTime(endTime));
        section.setName(sectionName);
        return section;
    }

    private Date getTime(String time) {
        DateFormat format = new SimpleDateFormat("hh:mm");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseLabName(String lab) {
        String[] split = lab.split(" ");
        return split[split.length - 1];
    }
}
