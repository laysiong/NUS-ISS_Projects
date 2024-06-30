package nus.iss.team07.laps.interfacemethods;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import nus.iss.team07.laps.model.LeaveType;

public interface LeaveTypeInterface {
	List<LeaveType> getAllLeaveTypes();
    Optional<LeaveType> getLeaveTypeById(int id);
    LeaveType saveLeaveType(LeaveType leaveType);
    void deleteLeaveType(LeaveType leaveType);
    
	LeaveType findIdByTypeLike(String leaveName);

}