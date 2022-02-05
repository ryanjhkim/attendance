package attendance.attendance.db;

import attendance.attendance.model.LabId;
import attendance.attendance.model.LabSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabRepository extends JpaRepository<LabSection, LabId> {

}
