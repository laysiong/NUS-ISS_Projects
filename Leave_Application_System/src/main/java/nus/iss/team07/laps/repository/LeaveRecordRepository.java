package nus.iss.team07.laps.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.LeaveRecord;

public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Integer>{
	
	
	
	@Query("SELECT r FROM LeaveRecord r WHERE r.status IN :statuses AND r.employee.supervisor.id =:id")
	 List<LeaveRecord> findLeaveRecordSupervisor(@Param("statuses") List<ApplicationStatus> statuses, @Param("id") int id);
	
	 @Query("SELECT r FROM LeaveRecord r WHERE r.employee = :employee ORDER BY r.startDate desc")
	 List<LeaveRecord> findByEmployeeId(@Param("employee") Employee employee);
	 
	 	 
	 // need to include application status (applied, updated, approved those to add) Other is to minus. 
	    @Query("SELECT SUM(l.numOfoff) " +
	            "FROM LeaveRecord l " +
	            "WHERE l.employee.id = :employeeId " +
	            "AND YEAR(l.startDate) = YEAR(:currentDate) " +
	            "AND l.leaveType.id = :leaveTypeId " +
	            "AND l.status IN :statuses " +
	            "GROUP BY l.leaveType.id")
	     Double sumLeaveTakenByEmployeeInYearAndType(
	         @Param("employeeId") int employeeId,
	         @Param("currentDate") LocalDate currentDate,
	         @Param("leaveTypeId") int leaveTypeId,
	         @Param("statuses") List<ApplicationStatus> statuses
	     );
	 
	 @Query("SELECT r FROM LeaveRecord r WHERE r.status IN :statuses")
	 List<LeaveRecord> findByStatus(@Param("statuses") List<ApplicationStatus> statuses);

	 @Query("SELECT lr FROM LeaveRecord lr WHERE lr.employee = :employee AND lr.status IN :statuses")
	 List<LeaveRecord> findByEmployeeAndStatusIn(@Param("employee") Employee employee, @Param("statuses") List<ApplicationStatus> statuses);
	
	 @Query("SELECT COUNT(r) > 0 FROM LeaveRecord r WHERE r.employee = :employee "
		        + "AND (:appliedLeaveStartDate <= r.endDate AND :appliedLeaveEndDate >= r.startDate) "
			    + "AND r.status IN :statuses "
		        + "AND r.id != :form_id")
		boolean existsOverlap(@Param("employee") Employee employee,
		                      @Param("appliedLeaveStartDate") LocalDate appliedStartDate, 
		                      @Param("appliedLeaveEndDate") LocalDate appliedEndDate,
		                      @Param("statuses") List<ApplicationStatus> statuses,
		                      @Param("form_id") int form_id); 
	 
}
