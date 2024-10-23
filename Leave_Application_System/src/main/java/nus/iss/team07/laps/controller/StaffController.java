package nus.iss.team07.laps.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import nus.iss.team07.laps.interfacemethods.CompensationInterface;
import nus.iss.team07.laps.interfacemethods.HolidayInterface;
import nus.iss.team07.laps.interfacemethods.LeaveCountInterface;
import nus.iss.team07.laps.interfacemethods.LeaveRecordInterface;
import nus.iss.team07.laps.interfacemethods.LeaveTypeInterface;
import nus.iss.team07.laps.model.ApplicationStatus;
import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.Holidays;
import nus.iss.team07.laps.model.LeaveCount;
import nus.iss.team07.laps.model.LeaveRecord;
import nus.iss.team07.laps.model.LeaveType;
import nus.iss.team07.laps.model.UserSession;
import nus.iss.team07.laps.service.CaculateOffDays;
import nus.iss.team07.laps.service.CompensationImplementation;
import nus.iss.team07.laps.service.EmployeeImplementation;
import nus.iss.team07.laps.service.HolidaysImplementation;
import nus.iss.team07.laps.service.LeaveCountImplementation;
import nus.iss.team07.laps.service.LeaveRecordImplementation;
import nus.iss.team07.laps.service.LeaveTypeImplementation;
import nus.iss.team07.laps.validator.DateValidator;



@Controller
public class StaffController {

	
	@Autowired
	private DateValidator dateValidator;
	
	@InitBinder
	private void initDateValidator(WebDataBinder binder) {
		binder.addValidators(dateValidator);
	}
	
	@Autowired
    private LeaveCountInterface leaveCountService;
	
	@Autowired
	private CompensationInterface CompensationService;

	@Autowired
    private LeaveRecordInterface leaveApplicationService;
	
	@Autowired
	private LeaveTypeInterface leaveTypeService;
	
	@Autowired
	private HolidayInterface holidayService;
	
	@Autowired
	private CaculateOffDays offDayService;
	
  // ------------------------
  // Main Menu
  // ------------------------
	
	//LIMIT to 5 
	@RequestMapping(value="/mainmenu")
    public String mainmenu(@SessionAttribute("usession") UserSession userSession, Model model) {
        //System.out.println("Roles set in session: " + userSession.getEmployee().getId());
		
	    List<LeaveCount> leaveCounts = leaveCountService.findLeaveCountsByEmployeeId(userSession.getEmployee().getId());
	    
        List<LeaveRecord> leaveRecords = leaveApplicationService.findByEmployeeId(userSession.getEmployee());
        List<LeaveRecord> filteredRecords = leaveRecords.stream()
	            .filter(record -> record.getStatus() == ApplicationStatus.Applied ||
	                              record.getStatus() == ApplicationStatus.Updated)
	            .limit(5)
	            .collect(Collectors.toList());
	    
	    model.addAttribute("leaveRecords", filteredRecords);
	    model.addAttribute("leaveCounts", leaveCounts);
	    return "mainmenu";
    }
	
	
	
  // ------------------------
  // Leaves ( CRUD )
  // ------------------------

	//VIEW
    @GetMapping("/leaveform")
    public String employeeleaveform(Model model) {
        // Add an empty LeaveApplication object to the model for Thymeleaf to bind to
    	List<LeaveType> allleavetype= leaveTypeService.getAllLeaveTypes();
    	
    	model.addAttribute("leavetype", allleavetype);
        model.addAttribute("leaveApplication", new LeaveRecord());
        return "LeaveForm";
    }

    
    @PostMapping("/leaveform/save")
    public String saveLeaveApplication(@Valid @ModelAttribute("leaveApplication") LeaveRecord leaveApplication,
                                       BindingResult bindingResult,
                                       @SessionAttribute("usession") UserSession userSession, 
                                       Model model) {
    	
    	// If it return back to form due to error, we need to add the Leave Type model back
    	List<LeaveType> allleavetype= leaveTypeService.getAllLeaveTypes();
    	model.addAttribute("leavetype", allleavetype);

    	
        if (bindingResult.hasErrors()) {
            model.addAttribute("leaveApplication", leaveApplication);
            return "LeaveForm";
        }
        
        
        
        // Grab Holidays and Weekends
        List<Holidays> publicholidays = holidayService.findAllHolidays();
        List<LocalDate> phList = publicholidays.stream()
                                               .map(Holidays::getStartDate)
                                               .collect(Collectors.toList());

        //countOff to count number of off require to take this application
        long countOff = offDayService.calculateLeaveDays(leaveApplication.getStartDate(), leaveApplication.getEndDate(), phList);
       
        Employee user = userSession.getEmployee();
        int userId = user.getId(); //get id
        int leaveType = leaveApplication.getLeaveType().getId();//get Leave Type from the application
        int form_id = leaveApplication.getId() == null ? -1 : leaveApplication.getId(); // if is new or updating
        LeaveType int_leaveType = leaveApplication.getInitialLeaveType();// get original LeaveType()
        
        
        // to get employee number of leaves he/she had (leave type chosen from application)
        LeaveCount EmpLeaveRecord = leaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(userId, leaveType);

      
        //if they pick weekend or public holidays
        if (countOff == 0) {
            model.addAttribute("leaveApplication", leaveApplication);
            model.addAttribute("errorMessage", "The dates you picked are weeked or public holidays.");
        	return "LeaveForm";
        }
        
        //If the leave type is still the same.
        //countOff (Current Application) - leaveApplication.getNumOfoff() is Origin Num of Off because it no save yet.
        if (EmpLeaveRecord.getAvailableLeave()+leaveApplication.getNumOfoff() < countOff) {
            model.addAttribute("leaveApplication", leaveApplication);
            model.addAttribute("errorMessage", "Not enough leaves available");
            return "LeaveForm";
        }
        
        //if startDate < EndDAte and EndAte > StartDAte
        if (leaveApplicationService.findOverlapRecord(user, leaveApplication.getStartDate(), leaveApplication.getEndDate(),  form_id)) {
            model.addAttribute("leaveApplication", leaveApplication);
            model.addAttribute("errorMessage", "There are overlap leave period, maybe you will like to check.");
            return "LeaveForm";
        }
        
        
        // Save Leave Application
        leaveApplication.setEmployee(user);
        leaveApplication.setNumOfoff(countOff);
        
        //new get Applied, edit get Updated
        leaveApplication.setStatus(leaveApplication.getId() == null ? ApplicationStatus.Applied : ApplicationStatus.Updated);
        leaveApplicationService.saveLeaveApplication(leaveApplication);

        
        //it is to sum all application that they taken
        Double sumOfTakenLeave_new = leaveApplicationService.getLeaveTakenByEmployeeInCurrentYearAndType(userId,leaveType);
       
       
    	//Update Emp Leave Count (Return back the leave)
        if (leaveType != int_leaveType.getId() && int_leaveType.getId() != 0) {
        	
        	//Update
        	LeaveCount Init_EmpLeaveRecord= leaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(userId,int_leaveType.getId());
            Double sumOfTakenLeave_old = leaveApplicationService.getLeaveTakenByEmployeeInCurrentYearAndType(userId,int_leaveType.getId());
            Init_EmpLeaveRecord.setTakenLeave(sumOfTakenLeave_old);
            
        	leaveCountService.saveLeaveCount(Init_EmpLeaveRecord);
        } 
        
        // Deal with different leave type and it for updating.
        if (leaveType != int_leaveType.getId() && int_leaveType.getId() != 0 && sumOfTakenLeave_new > EmpLeaveRecord.getAvailableLeave()) {
        	  model.addAttribute("leaveApplication", leaveApplication);
              model.addAttribute("errorMessage", "Not enough leaves available"); // deal with different leave types
              return "LeaveForm";
        }      
        
        EmpLeaveRecord.setTakenLeave(sumOfTakenLeave_new);
    	leaveCountService.saveLeaveCount(EmpLeaveRecord);

        return "redirect:/appliedLeaveRecords"; // Redirect back to the home page
    }

    
    
	//VIEW
	@GetMapping("/appliedLeaveRecords")
    public String getAppliedLeaveRecords(@SessionAttribute("usession") UserSession userSession, Model model) {
      List<LeaveRecord> leaveRecords = leaveApplicationService.findByEmployeeId(userSession.getEmployee());
      List<LeaveRecord> filteredRecords = leaveRecords.stream()
            .filter(record -> record.getStatus() == ApplicationStatus.Applied ||
                              record.getStatus() == ApplicationStatus.Updated)
            .collect(Collectors.toList());
		
      	model.addAttribute("title", "Leave Application");
        model.addAttribute("leaveRecords", filteredRecords);
        return "leaveRecordHistory"; // Return the appropriate view name
    }
	
	
	
	//VIEW
	 @GetMapping("/leaveRecordsByEmployee")
	 public String getLeaveRecordsByEmployee(@SessionAttribute("usession") UserSession userSession, Model model) {
		 
	        List<LeaveRecord> leaveRecords = leaveApplicationService.findByEmployeeId(userSession.getEmployee());
	        List<LeaveRecord> filteredRecords = leaveRecords.stream()
	                .filter(record -> record.getStatus() != ApplicationStatus.Applied &&
	                                  record.getStatus() != ApplicationStatus.Updated)
	                .collect(Collectors.toList());
	        
	        model.addAttribute("title", "Leave Record History");
	        model.addAttribute("leaveRecords", filteredRecords);
	        return "leaveRecordHistory"; // Return the appropriate view name
	 }
	 
	 
	 //CANCEL
	 @RequestMapping("/cancelLeaveRecord/{id}")
     public String cancelLeaveRecord(@PathVariable("id") int formId) {
	
		 Optional<LeaveRecord> leaveRecordOpt = leaveApplicationService.findformById(formId);
		 
		 //need to check if the Date is over DateNow() if is not need to return.
		 leaveRecordOpt.ifPresent(leaveRecord -> {
		       	int userId = leaveRecord.getEmployee().getId();
		    	int leaveType = leaveRecord.getLeaveType().getId();
			 
		        leaveRecord.setStatus(ApplicationStatus.Cancel);
		        leaveApplicationService.saveLeaveApplication(leaveRecord);
		        
		        // to get employee remainder leaves record
		        LeaveCount EmpLeaveRecord= leaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(userId, leaveType);

		        //number of off required to be taken
		        Double NumOfOff = leaveApplicationService.getLeaveTakenByEmployeeInCurrentYearAndType(userId, leaveType);
		        	
		        EmpLeaveRecord.setTakenLeave(NumOfOff);
		    	leaveCountService.saveLeaveCount(EmpLeaveRecord);
		    });
		
		return "redirect:/leaveRecordsByEmployee";
     }
	 
	 
	 //DELETE
	 @RequestMapping("/deleteLeaveRecord/{id}")
     public String deleteLeaveRecord(@PathVariable("id") int formId) {
	
		 Optional<LeaveRecord> leaveRecordOpt = leaveApplicationService.findformById(formId);
		 
		 leaveRecordOpt.ifPresent(leaveRecord -> {
		       	int userId = leaveRecord.getEmployee().getId();
		    	int leaveType = leaveRecord.getLeaveType().getId();
			 
		        leaveRecord.setStatus(ApplicationStatus.Deleted);
		        leaveApplicationService.saveLeaveApplication(leaveRecord);
		        
		        // to get employee remainder leaves record
		        LeaveCount EmpLeaveRecord= leaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(userId, leaveType);

		        //number of off required to be taken
		        Double NumOfOff = leaveApplicationService.getLeaveTakenByEmployeeInCurrentYearAndType(userId, leaveType);
		        	
		        EmpLeaveRecord.setTakenLeave(NumOfOff);
		    	leaveCountService.saveLeaveCount(EmpLeaveRecord);
		    });
		
		return "redirect:/leaveRecordsByEmployee";
     }

	 
	//EDIT
	@RequestMapping(value = "/editLeaveRecord/{id}")
	public String editLeaveApplicationForm(@PathVariable("id") int formId,
											Model model) {
		
	  LeaveRecord LeaveApplication = leaveApplicationService.findformById(formId).get();	
      List<LeaveType> allleavetype= leaveTypeService.getAllLeaveTypes();
	
      if (LeaveApplication != null) {
          LeaveApplication.setInitialLeaveType(LeaveApplication.getLeaveType());
    	  model.addAttribute("leaveApplication", LeaveApplication);
    	  model.addAttribute("leavetype", allleavetype);
          return "LeaveForm";

      }
      return "redirect:/appliedLeaveRecords";
	}
       	   
    //view form details
	@RequestMapping(value = "/detailLeaveRecord/{id}")
	public String detailLeaveApplicationForm(@PathVariable("id") int formId, HttpServletRequest request,
											Model model) {
		
	   Optional<LeaveRecord> LeaveApplication = leaveApplicationService.findformById(formId);	
	
      if (LeaveApplication.isPresent()) {
 	      String referer = request.getHeader("Referer");
    	  model.addAttribute("leaveRecord", LeaveApplication.get());
	      model.addAttribute("referer", referer); 

          return "LeaveRecordDetails";
      }
      
      return "redirect:/leaveRecordsByEmployee";
	}
       
    

    

	/* -------------------------------------------------------------------------------------------------------------  */
    
	/* 
	 * Compensation 
	*/
	    	

	
	// View All Compensation List
	@RequestMapping(value="/Compensationlist")
	public String list(
            @SessionAttribute("usession") UserSession userSession, 
            Model model) {
		
		model.addAttribute("CompensationList", CompensationService.getPersonalCompensationHistory(userSession.getEmployee()));
		return "CompensationHistory";
	}
	
	
	/*
	// Cancel - We should be canceling our claim after it is approved right?
    @RequestMapping(value = "/compensation/cancel/{id}")
    public String cancelLeaveType(@PathVariable("id") Integer id, Model model) {
        Optional<Compensation> CompensationForm = CompensationService.findCompensationById(id);
        //Really deleted it
        //CompensationForm.ifPresent(value -> CompensationService.deleteCompensationForm(value));
       
        //update to deleted
        if (CompensationForm.isPresent()) {
            Compensation compensation = CompensationForm.get();
            compensation.setC_status(ApplicationStatus.Cancel);
            CompensationService.saveCompensation(compensation); 
        }
        
        return "redirect:/Compensationlist";
    }
	*/
	
	
	@RequestMapping("/compensation/view/{id}")
	public String viewCompensation(@PathVariable("id") Integer id, Model model,  HttpServletRequest request) {
	    Optional<Compensation> compensation = CompensationService.findCompensationById(id);
	    if (compensation.isPresent()) {
	        model.addAttribute("compensation", compensation.get());
	        String referer = request.getHeader("Referer");
	        model.addAttribute("referer", referer); // Add referer URL to the model
	        return "CompensationDetails";
	    }
	    return "redirect:/Compensationlist"; // Redirect if not found
	}
	
	
	
	// Deleted
    @RequestMapping(value = "/compensation/delete/{id}")
    public String deleteLeaveType(@PathVariable("id") Integer id, Model model) {
        Optional<Compensation> CompensationForm = CompensationService.findCompensationById(id);
        //Really deleted it
        //CompensationForm.ifPresent(value -> CompensationService.deleteCompensationForm(value));
       
        //update to deleted
        if (CompensationForm.isPresent()) {
            Compensation compensation = CompensationForm.get();
            compensation.setC_status(ApplicationStatus.Deleted);
            CompensationService.saveCompensation(compensation); 
        }
        
        return "redirect:/Compensationlist";
    }
	 

	// Edit compensation form (Updated)
    @RequestMapping("/compensation/edit/{id}")
    public String editCompensationForm(@PathVariable("id") Integer id, Model model) {
		
        Optional<Compensation> compensation = CompensationService.findCompensationById(id);
        Compensation comp = compensation.get();
        //compensation form not found
        if (compensation.isPresent() && 
            (comp.getC_status() == ApplicationStatus.Applied || comp.getC_status() == ApplicationStatus.Updated)) {
            model.addAttribute("CompensationForm", comp);

            return "CompensationForm"; 
        }
        return "redirect:/Compensationlist";
    }
	
    //EditCompensationForm
	@RequestMapping(value = "/compensation/save")
	public String saveProduct(@ModelAttribute("CompensationForm") @Valid Compensation CompensationForm,
			BindingResult bindingResult,Model model) {
		
        Optional<Compensation> originalCompensation = CompensationService.findCompensationById(CompensationForm.getCompensationId());
		if (bindingResult.hasErrors()) {
			return "CompensationForm";
		}
	    
		Compensation original = originalCompensation.get();
		CompensationForm.setEmployee(original.getEmployee());
	    CompensationForm.setLeaveType(original.getLeaveType());
		CompensationForm.setC_status(ApplicationStatus.Updated);
		CompensationService.saveCompensation(CompensationForm);
		return "redirect:/Compensationlist";
	}
	
	
	
	// Create Compensation List
	@GetMapping(value="/CompensationForm")
	public String addForm(Model model) {
		model.addAttribute("CompensationForm", new Compensation());
		return "CompensationForm";
	}
	
	
    @PostMapping(value = "/compensation/create")
    public String saveCompensation(@ModelAttribute("CompensationForm") @Valid Compensation compensationForm, 
                                   BindingResult bindResults, 
                                   @SessionAttribute("usession") UserSession userSession, 
                                   Model model) {
        if (bindResults.hasErrors()) {
        	model.addAttribute("CompensationForm", compensationForm);
            return "CompensationForm"; // Return to form with validation errors
        }

        // Set the employee ID from the session
        LeaveType compensationLeaveType = leaveTypeService.findIdByTypeLike("compensation");
        
        compensationForm.setLeaveType(compensationLeaveType);
        compensationForm.setEmployee(userSession.getEmployee());
        compensationForm.setC_status(ApplicationStatus.Applied);

        CompensationService.saveCompensation(compensationForm);
        return "redirect:/Compensationlist";
    }
	
	
}
