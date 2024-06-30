package nus.iss.team07.laps.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import nus.iss.team07.laps.interfacemethods.LeaveCountInterface;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.LeaveCount;
import nus.iss.team07.laps.model.LeaveType;
import nus.iss.team07.laps.model.Role;
import nus.iss.team07.laps.repository.EmployeeRepository;
import nus.iss.team07.laps.repository.LeaveCountRepository;
import nus.iss.team07.laps.repository.LeaveTypeRepository;
import nus.iss.team07.laps.repository.RoleRepository;

@Service
public class LeaveCountImplementation implements LeaveCountInterface {

    @Autowired
    private LeaveCountRepository leaveCountRepository;
        
    @Autowired
    private LeaveTypeRepository leaveTypeRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository; 
    
    @Transactional
	public void deleteLeaveRecordByEmployeeId(int employeeId) {
    	
        List<LeaveCount> leaveCounts = leaveCountRepository.findByEmployeeId(employeeId);
        if (!leaveCounts.isEmpty()) {
            for (LeaveCount leaveCount : leaveCounts) {
                leaveCountRepository.delete(leaveCount);
            }
        }
    }
    

    @Override
    public List<LeaveCount> findAllLeaveCounts() {
        return leaveCountRepository.findAll();
    }

    @Override
    public List<LeaveCount> findLeaveCountsByEmployeeId(int employeeId) {
        // Implement logic to find leave counts by employee ID
        return leaveCountRepository.findByEmployeeId(employeeId);
    }
    
    

    @Override
    public LeaveCount findLeaveCountByEmployeeIdAndLeaveTypeId(int employeeId, int leaveTypeId) {
        // Implement logic to find a specific leave count by employee ID and leave type ID
        return leaveCountRepository.findByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId);
    }

    @Override
    @Transactional
    public void saveLeaveCount(LeaveCount leaveCount) {
        leaveCountRepository.save(leaveCount);
    }

	public LeaveCount findByEmployeeIdAndLeaveTypeId(Integer employeeId, Integer leaveTypeId) {
		
		return leaveCountRepository.findByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId);
	}


	//see if can add leave type
	public List<LeaveCount> searchLeaveEntitlements(String criteria, String keyword) {
        switch (criteria) {
            case "employeeName":
                return leaveCountRepository.findByEmployeeNameContainingIgnoreCase(keyword);
            case "employeeId":
                return leaveCountRepository.findByEmployeeId(Integer.parseInt(keyword));
            case "department":
                return leaveCountRepository.findByDepartmentNameContainingIgnoreCase(keyword);
            case "role":
                return leaveCountRepository.findByRoleTypeContainingIgnoreCase(keyword);
            default:
                throw new IllegalArgumentException("Invalid search criteria: " + criteria);
        }
    }


	@Transactional
	public void createCountforNewEmp(Employee employee) {
        List<LeaveType> leaveList = leaveTypeRepository.findAll();
	    Optional<Role> role_type = roleRepository.findById(employee.getRole().getId());
        List<String> roles = Arrays.asList("Admin", "Manager");

	    //each leave time have a template.
	    try {

	        for (LeaveType leaveType : leaveList) {
	            double entitledLeave = leaveType.getDefault_val(); // Default initialization
	            
	            //for now have hard code this in
	            if (leaveType.getId() == 2) {
	                entitledLeave = roles.contains(role_type.get().getType()) ? 18.0 : 14.0;
	            }
	            
	            LeaveCount newEmpCount = new LeaveCount(employee, leaveType, entitledLeave);
	            leaveCountRepository.save(newEmpCount);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Error in createCountforNewEmp: " + e.getMessage());
	    }
	}


	
	
	public void createLeaveCountforNewLeaveType (LeaveType leave) {
		List<Employee> employeeList = employeeRepository.findAll();
		
		try {
			
			for (Employee employee: employeeList) {
				LeaveCount newLeaveType = new LeaveCount(employee, leave, 0.0);
				leaveCountRepository.save(newLeaveType);
			}
			
		} catch (Exception e) {
	        e.printStackTrace();
		}
			
	}

	
	public void resetLeaveCountForNewYear() {
	    List<Employee> employeeList = employeeRepository.findAll();
	    List<LeaveType> leaveList = leaveTypeRepository.findAll();
	    List<String> roles = Arrays.asList("Admin", "Manager");

	    for (Employee employee : employeeList) {
	        for (LeaveType leaveType : leaveList) {
	            double entitledLeave = leaveType.getDefault_val(); // Default initialization

	            if (leaveType.getId() == 2) {
	                entitledLeave = roles.contains(employee.getRole().getType()) ? 18.0 : 14.0;
	                
	                //Bring frontward Off
	                try {
	                    LeaveCount previousLeaveCount = leaveCountRepository.findByEmployeeIdAndLeaveTypeId(employee.getId(), leaveType.getId());
	                    entitledLeave += previousLeaveCount.getAvailableLeave()/2;
	                    entitledLeave = Math.min(entitledLeave, 21);
	                } catch (Exception e) {
	                    System.out.println("No previous leave count found for employee ID: " + employee.getId() + " and leave type ID: " + leaveType.getId());
	                }
	                
	            }

	            //make sure it existed, we getting correct record
	            LeaveCount existingLeaveCount = leaveCountRepository.findByEmployeeIdAndLeaveTypeId(employee.getId(), leaveType.getId());
	            
	            if (existingLeaveCount != null) {
	            	//update
	            	System.out.println(entitledLeave);
	                existingLeaveCount.setTotalLeave(entitledLeave);
	                existingLeaveCount.setTakenLeave(0);
	                leaveCountRepository.save(existingLeaveCount);
	            } else {
	                LeaveCount newEmpCount = new LeaveCount(employee, leaveType, entitledLeave);
	                leaveCountRepository.save(newEmpCount);
	            }
	            
	            
	        }
	    }
	}


	
	
	

	
/*    public List<LeaveCount> searchLeaveEntitlements(String criteria, String keyword) {
        switch (criteria) {
            case "employeeName":
                return leaveCountRepository.findByEmployeeNameContainingIgnoreCase(keyword);
            case "employeeId":
                return leaveCountRepository.findByEmployeeId(Integer.parseInt(keyword));
            case "department":
                return leaveCountRepository.findByDepartmentNameContainingIgnoreCase(keyword);
            case "role":
                return leaveCountRepository.findByRoleTypeContainingIgnoreCase(keyword);
            case "leaveType":
                return leaveCountRepository.findByLeaveTypeTypeContainingIgnoreCase(keyword);
            default:
                throw new IllegalArgumentException("Invalid search criteria: " + criteria);
        }
    }
*/
}