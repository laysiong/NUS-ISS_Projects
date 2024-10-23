package nus.iss.team07.laps.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import nus.iss.team07.laps.model.Compensation;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.LeaveRecord;
import nus.iss.team07.laps.service.CompensationImplementation;


@Component
public class DateValidator implements Validator {
	
    
   @Override
   public boolean supports(Class<?> clazz) {
       return LeaveRecord.class.isAssignableFrom(clazz) || Compensation.class.isAssignableFrom(clazz);
   }
	
	@Override
	public void validate(Object obj, Errors errors) {
	    //System.out.println("---- DateValidator validate() -----");
	    
	    
	    if (obj instanceof LeaveRecord) {
	        LeaveRecord leaveRecord = (LeaveRecord) obj;
	        
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "error.startDate", "Start date is required");
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "error.endDate", "End date is required");
	        
	        if ((leaveRecord.getStartDate() != null && leaveRecord.getEndDate() != null) &&
	                (leaveRecord.getStartDate().compareTo(leaveRecord.getEndDate()) > 0)) {
	            System.out.println("End Date must be later than Start Date");
	            errors.rejectValue("endDate", "error.endDateEarly", "End Date must be later than Start Date");
	        }
	    } else if (obj instanceof Compensation) {
	        Compensation compensation = (Compensation) obj;

	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "error.startDate", "Start date is required");

	        if (compensation.getStartDate() != null && compensation.getStartDate().compareTo(LocalDate.now()) > 0) {
	            errors.rejectValue("startDate", "error.dates", "Please note that the date cannot be later than today.");
	        }
	    } else {
	        throw new IllegalArgumentException("Unsupported object type: " + obj.getClass().getName());
	    }
	  
	}
}
