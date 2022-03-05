package attendance.attendance.service;

import attendance.attendance.db.AttendanceRepository;
import attendance.attendance.model.Attendance;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final AttendanceRepository _attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        _attendanceRepository = attendanceRepository;
    }

    public void RecordAttendance(Attendance record) {
        _attendanceRepository.save(record);
    }
}
