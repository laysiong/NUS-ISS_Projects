package nus.iss.team07.laps.repository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;

//Integer refer to our compensation unique ID
public interface CompensationRepository extends JpaRepository<Compensation, Integer>{
	
	 @Query("SELECT c FROM Compensation c WHERE c.C_status IN :statuses")
	 List<Compensation> findByStatusIn(@Param("statuses") List<ApplicationStatus> statuses);
	
	
	 @Query("SELECT c FROM Compensation c WHERE c.employee = :employee ORDER BY c.C_status desc, c.startDate desc")
	 List<Compensation> findByEmployee(@Param("employee") Employee employee);

	 
	
}
