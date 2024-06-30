package nus.iss.team07.laps.repository;

import nus.iss.team07.laps.model.LeaveCount;
import nus.iss.team07.laps.model.CompositeKey1;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;


public interface LeaveCountRepository extends JpaRepository<LeaveCount, CompositeKey1> {
	
	 //For Composite Table
	 List<LeaveCount> findByEmployeeId(int employeeId);
	 
	 @Query("SELECT lc FROM LeaveCount lc WHERE lc.employee.id = :employeeId and lc.leaveType.id= :leaveTypeId")
	 LeaveCount findByEmployeeIdAndLeaveTypeId(@Param("employeeId") int employeeId, @Param("leaveTypeId") int leaveTypeId);
	 
	 @Query("SELECT lc FROM LeaveCount lc WHERE lc.employee.name LIKE %:keyword%")
	 List<LeaveCount> findByEmployeeNameContainingIgnoreCase(@Param("keyword") String keyword);
	 
	 @Query("SELECT lc FROM LeaveCount lc WHERE lc.employee.department.name LIKE %:keyword%")
	 List<LeaveCount> findByDepartmentNameContainingIgnoreCase(@Param("keyword") String keyword);

	 @Query("SELECT lc FROM LeaveCount lc WHERE lc.employee.role.type LIKE %:keyword%")
	 List<LeaveCount> findByRoleTypeContainingIgnoreCase(@Param("keyword") String keyword);


	 
}