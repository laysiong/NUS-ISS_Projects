package nus.iss.team07.laps.interfacemethods;

import java.util.List;

import jakarta.validation.Valid;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.LeaveCount;
import nus.iss.team07.laps.model.LeaveType;

public interface LeaveCountInterface {
    List<LeaveCount> findAllLeaveCounts();
    List<LeaveCount> findLeaveCountsByEmployeeId(int employeeId);
    LeaveCount findLeaveCountByEmployeeIdAndLeaveTypeId(int employeeId, int leaveTypeId);
    void saveLeaveCount(LeaveCount leaveCount);
    void deleteLeaveRecordByEmployeeId(int employeeId);
	
	public void createCountforNewEmp (Employee employee);
	void createLeaveCountforNewLeaveType(@Valid LeaveType leaveType);
	
	List<LeaveCount> searchLeaveEntitlements(String criteria, String keyword);
	void resetLeaveCountForNewYear();
}
