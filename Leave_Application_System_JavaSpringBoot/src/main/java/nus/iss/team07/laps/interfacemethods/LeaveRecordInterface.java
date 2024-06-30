package nus.iss.team07.laps.interfacemethods;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.LeaveRecord;

public interface LeaveRecordInterface {
    LeaveRecord saveLeaveApplication(LeaveRecord leaveApplication);
    
	List<LeaveRecord> findLeaveRecordSupervisor(List<ApplicationStatus>status, int id);
	List<LeaveRecord> findByEmployeeId(Employee employeeId);
	
	List<LeaveRecord> findByStatus(List<ApplicationStatus>status);
	void deleteLeaveRecord(LeaveRecord leaveApplication);

	Double getLeaveTakenByEmployeeInCurrentYearAndType(int employeeId, int leaveTypeId);

	boolean findOverlapRecord(Employee user, LocalDate startDate, LocalDate endDate, int form_id);

	Optional<LeaveRecord> findformById(int formId);

	List<LeaveRecord> findAppliedOrUpdatedLeaveRecordsByEmployee(Employee employee);


	
}
