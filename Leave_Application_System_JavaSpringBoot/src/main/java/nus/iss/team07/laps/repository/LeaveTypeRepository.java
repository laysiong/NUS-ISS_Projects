package nus.iss.team07.laps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.LeaveType;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer>{
	

	/* this is to ensure if in a case compensation will to shifted */
	@Query("SELECT lt FROM LeaveType lt WHERE LOWER(lt.type) LIKE LOWER(CONCAT('%', :leaveName, '%'))")
	LeaveType findByTypeLike(@Param("leaveName") String leaveName);
	
}
