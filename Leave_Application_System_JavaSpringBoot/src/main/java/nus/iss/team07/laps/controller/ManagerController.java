package nus.iss.team07.laps.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import nus.iss.team07.laps.interfacemethods.CompensationInterface;
import nus.iss.team07.laps.interfacemethods.EmployeeInterface;
import nus.iss.team07.laps.interfacemethods.LeaveCountInterface;
import nus.iss.team07.laps.interfacemethods.LeaveRecordInterface;
import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;

import nus.iss.team07.laps.model.LeaveCount;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.LeaveRecord;
import nus.iss.team07.laps.model.UserSession;


@Controller
//@RequestMapping("/manager")
@SessionAttributes(value = {"usession"}, types = {UserSession.class})

public class ManagerController {

	
	/*
	 * Approve/Reject Leave
	 * Approve Compensation Claim
	 * 
	 */
	
	@Autowired
	private CompensationInterface CompensationService;

	/* new added */
	@Autowired
	private LeaveRecordInterface lrservice;

	@Autowired
	private LeaveCountInterface LeaveCountService;
	
	@Autowired
	private EmployeeInterface eservice;
	
	
	//view get user department and check his employee 
	//applied updated
	@GetMapping("/pendingrecords")
	public String getLeaveRecords(ModelMap model, HttpSession session) {
		UserSession usession = (UserSession) session.getAttribute("usession");
		int id = usession.getEmployee().getId();
		List<Employee> employees = eservice.getEmployeeSupervisor(id);
		
	    employees.forEach(employee -> {
	        List<LeaveRecord> filteredLeaveRecords = lrservice.findAppliedOrUpdatedLeaveRecordsByEmployee(employee);
	        employee.setLeaverecords(filteredLeaveRecords);
	    });
		
		model.addAttribute("employees", employees);
		
		return "Manager/display-leaves";
	}
	
	 @GetMapping("/pendingrecords/search")
	    public String searchLeaveEntitlements(@RequestParam String keyword, 
	    										HttpSession session,
	    										RedirectAttributes redirectAttributes,
	    										Model model) {
	    	//System.out.println(keyword);
    		UserSession usession = (UserSession) session.getAttribute("usession");
    		int id = usession.getEmployee().getId();
	 		List<Employee> employees = eservice.getEmpUnderUs(id, keyword);
			
		    employees.forEach(employee -> {
		        List<LeaveRecord> filteredLeaveRecords = lrservice.findAppliedOrUpdatedLeaveRecordsByEmployee(employee);
		        employee.setLeaverecords(filteredLeaveRecords);
		    });


	        model.addAttribute("employees", employees);
	        
	        if (employees.isEmpty()) {
	        	redirectAttributes.addFlashAttribute("message", "No results found.");
	            System.out.println("nothing here");
	            return "redirect:/pendingrecords";
	        }

	        return "Manager/display-leaves";
	    }
	
	
	
	
	//View approved/ rejected/ cancel /detail
	@GetMapping("/leaverecords")
	public String getPendingRecords(Model model, HttpSession session) {
		UserSession usession = (UserSession) session.getAttribute("usession");
		int id = usession.getEmployee().getId();
        List<ApplicationStatus> statuses = Arrays.asList(ApplicationStatus.Approved, ApplicationStatus.Reject, ApplicationStatus.Cancel);
		model.addAttribute("leaves", lrservice.findLeaveRecordSupervisor(statuses, id));
		
		return "Manager/display-precords";
	}
	
	// Approve / Reject Leave Application
	@PostMapping("/Lapplication_status/{id}")
	public String updateLApplicationStatus(@PathVariable("id") Integer id, 
	                                       @RequestParam("status") String status,
	                                       @RequestParam("comment") String comment,
	                                       RedirectAttributes redirectAttributes) {
	    Optional<LeaveRecord> recordOpt = lrservice.findformById(id);

	    if (!recordOpt.isPresent()) {
	        return "redirect:/pendingrecords"; // remove /manager
	    }

	    LeaveRecord leaveRecord = recordOpt.get();

	    // Entry for Emp Leave Count/Remainder - Total Leave Taken - Total Leave Available
	    LeaveCount employeeLeaveCount = LeaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(leaveRecord.getEmployee().getId(), leaveRecord.getLeaveType().getId());

	    if ("Reject".equals(status) && (comment == null || comment.trim().isEmpty())) {
	        redirectAttributes.addFlashAttribute("error", "Please provide a reason for rejection.");
	        redirectAttributes.addFlashAttribute("Id", id); // Ensure this matches the attribute being checked in the form
	        return "redirect:/pendingrecords"; //remove /manager
	    }

	    switch (status) {
	        case "Approve":
	            leaveRecord.setStatus(ApplicationStatus.Approved);
	            break;
	        case "Reject":
	            leaveRecord.setStatus(ApplicationStatus.Reject);
	            break;
	        default:
	            // Handle unknown status if necessary
	            break;
	    }

	    leaveRecord.setComment(comment);
	    lrservice.saveLeaveApplication(leaveRecord);

	    int userId = leaveRecord.getEmployee().getId();
	    int leaveType = leaveRecord.getLeaveType().getId();

	    LeaveCount EmpLeaveRecord = LeaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(userId, leaveType);
	    Double sumOfTakenLeave = lrservice.getLeaveTakenByEmployeeInCurrentYearAndType(userId, leaveType);
	    EmpLeaveRecord.setTakenLeave(sumOfTakenLeave != null ? sumOfTakenLeave : 0.0);
	    LeaveCountService.saveLeaveCount(EmpLeaveRecord);

        return "redirect:/pendingrecords"; //remove /manager
	}
	
	
	// View All Compensation List
	@RequestMapping(value="/compensation_applicaiton")
	public String list(Model model) {
	    List<Compensation> compensations = CompensationService.findPendingAndApprovedCompensations();
		model.addAttribute("CompensationList", compensations);
		return "/Manager/CompensationApplication";
	}
	
	
	// Approve / Reject Compensation Claim
   @PostMapping("/application_status/{id}")
    public String updateApplicationStatus(@PathVariable("id") Integer id, 
                                          @RequestParam("status") String status,
                                          @RequestParam("C_comment") String comment,
                                          RedirectAttributes redirectAttributes, Model model) {
        Optional<Compensation> compensationOpt = CompensationService.findCompensationById(id);
        
        if (!compensationOpt.isPresent()) {
            return "redirect:/compensation_application"; //remove /manager
        }
        
        Compensation compensation = compensationOpt.get();

        LeaveCount employeeCompensationCount = LeaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(compensation.getEmployee().getId(),compensation.getLeaveType().getId());

        if ("Reject".equals(status) && (comment == null || comment.trim().isEmpty())) {
            redirectAttributes.addFlashAttribute("error", "Please provide a <br> reason for rejection.");
            redirectAttributes.addFlashAttribute("compensationId", id);
            return "redirect:/compensation_applicaiton"; //remove /manager
        }
           
        switch (status) {
            case "Approve":
                compensation.setC_status(ApplicationStatus.Approved);
                employeeCompensationCount.addCompensationLeave(compensation.getHours());
               
                break;
            case "Reject":
                compensation.setC_status(ApplicationStatus.Reject);
                break;
            default:
                // Handle unknown status if necessary
                break;
        }
        
	    compensation.setC_comment(comment);
        CompensationService.saveCompensation(compensation);
        return "redirect:/compensation_applicaiton"; //remove /manager
    }
	
	
	
	
}




