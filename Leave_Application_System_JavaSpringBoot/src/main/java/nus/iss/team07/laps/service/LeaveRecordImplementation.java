package nus.iss.team07.laps.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import nus.iss.team07.laps.interfacemethods.LeaveRecordInterface;
import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.CompositeKey2;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.LeaveRecord;
import nus.iss.team07.laps.repository.LeaveRecordRepository;



@Service
@Transactional
public class LeaveRecordImplementation implements LeaveRecordInterface {
	
	@Autowired
	private LeaveRecordRepository leaveRecordrepository;

	/* Staff Level */
	
	public Optional<LeaveRecord> findformById(int id) {
		return leaveRecordrepository.findById(id);
	}
	
	
	@Override
	public List<LeaveRecord> findLeaveRecordSupervisor(List<ApplicationStatus> status, int id) {
		return leaveRecordrepository.findLeaveRecordSupervisor(status, id);

	}
	
	@Override
	public List<LeaveRecord> findByStatus(List<ApplicationStatus> status) {
		return leaveRecordrepository.findByStatus(status);

	}

	@Override
	public List<LeaveRecord> findByEmployeeId(Employee employeeId) {
		return leaveRecordrepository.findByEmployeeId(employeeId);
	}
		
		
    @Override
	@Transactional
    public LeaveRecord saveLeaveApplication(LeaveRecord leaveApplication) {
        return leaveRecordrepository.save(leaveApplication);
    }

	@Override
	@Transactional
	public void deleteLeaveRecord(LeaveRecord leaveApplication) {
		leaveRecordrepository.delete(leaveApplication);
	}

    @Override
	@Transactional
	public Double getLeaveTakenByEmployeeInCurrentYearAndType(int employeeId, int leaveTypeId) {
    	List<ApplicationStatus> apStatus = Arrays.asList(ApplicationStatus.Approved, ApplicationStatus.Applied, ApplicationStatus.Updated);

    	LocalDate currentDate = LocalDate.now(); // Get the current date
        int currentYear = currentDate.getYear(); // Get the current year
        
        Double sum = leaveRecordrepository.sumLeaveTakenByEmployeeInYearAndType(employeeId, currentDate, leaveTypeId, apStatus);
        return sum != null ? sum : 0.0;
        
	}
     
    public List<LeaveRecord> findAppliedOrUpdatedLeaveRecordsByEmployee(Employee employee) {
        return leaveRecordrepository.findByEmployeeAndStatusIn(employee, Arrays.asList(ApplicationStatus.Applied, ApplicationStatus.Updated));
    }
    
	  
    public boolean findOverlapRecord (Employee employee, LocalDate startDate, LocalDate endDate,int form_id){
    	
    	List<ApplicationStatus> status = Arrays.asList(ApplicationStatus.Applied, ApplicationStatus.Updated, ApplicationStatus.Approved);
    	return  leaveRecordrepository.existsOverlap(employee, startDate, endDate, status, form_id);
    }
    
    
	
}
