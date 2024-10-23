package nus.iss.team07.laps.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.team07.laps.dto.SupervisorDTO;
import nus.iss.team07.laps.interfacemethods.EmployeeInterface;
import nus.iss.team07.laps.interfacemethods.HolidayInterface;
import nus.iss.team07.laps.model.Holidays;
import nus.iss.team07.laps.service.DepartmentService;
import nus.iss.team07.laps.service.RoleService;



@RestController
@RequestMapping("/api")
public class restController {
	
	  @Autowired
	  private HolidayInterface holidayService;
	  
   	  @Autowired
	  private EmployeeInterface employeeInterface;

	  @Autowired
	  private DepartmentService departmentService;

	  @Autowired
	  private RoleService roleService;
		

	  @GetMapping("/holiday")
	  public List<Holidays> getAllHolidays() {
	    return holidayService.findAllHolidays();
	  }
	  
	  @GetMapping("/holiday/{id}")
	  public ResponseEntity<Holidays> getHolidaysById(@PathVariable int id) {
	    Optional<Holidays> optCourse = holidayService.findHolidayById(id);
	    
	    if (optCourse.isPresent()) {
	      Holidays course = optCourse.get();
	      return new ResponseEntity<Holidays>(course, HttpStatus.OK);
	    } else {
	      return new ResponseEntity<Holidays>(HttpStatus.NOT_FOUND);
	    }
	  }

	  
	  @PostMapping("/holiday")
	  public ResponseEntity<Holidays> create(@RequestBody Holidays inHolidays) {
	    try {
	    Holidays retHoliday = holidayService.saveHolidays(inHolidays);      
	      
	      return new ResponseEntity<Holidays>(retHoliday, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    }
	  }
	  
	  
	  @PostMapping("/holiday/{id}")
	  public ResponseEntity<Holidays> editCourse(@PathVariable("id") int id, @RequestBody Holidays inHolidays) {
	    Optional<Holidays> optHoliday = holidayService.findHolidayById(id);
	    
	    if (optHoliday.isPresent()) {
	      // Update the managed course obj
	     Holidays holiday = optHoliday.get();
	      
	      holiday.setHoliday(inHolidays.getHoliday());
	      holiday.setStartDate(inHolidays.getStartDate());
	      
	      Holidays updatedholiday = holidayService.saveHolidays(holiday);
	      
	      return new ResponseEntity<Holidays>(updatedholiday, HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  
	  @DeleteMapping("/holiday/{id}")
	  public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") int id) {
	    try {
	      holidayService.deleteHolidaybyId(id);
	      return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
	    }
	  }

		@GetMapping("/supervisors")
		public ResponseEntity<List<SupervisorDTO>> getSupervisors(@RequestParam(required = false) Integer departmentId,
				@RequestParam(required = false) Integer roleId) {

			try {
				List<Integer> defaultDepartmentId = new ArrayList<Integer>();
				List<Integer> defaultRoleId = new ArrayList<Integer>();
				if (departmentId == null) {
					defaultDepartmentId = departmentService.findAllDepartments().stream().map(d -> d.getId())
							.collect(Collectors.toList());
				} else {
					defaultDepartmentId.add(departmentId);
				}
				if (roleId == null) {
					defaultRoleId = roleService.findAllRoles().stream().map(r -> r.getId()).collect(Collectors.toList());
				} else {
					defaultRoleId.add(roleId);
				}
				List<SupervisorDTO> employees = employeeInterface
						.findSupervisorByDepartmentAndRole(defaultDepartmentId, defaultRoleId).stream()
						.filter(e -> e.getSupervisor() != null)
						.map(e -> new SupervisorDTO(e.getSupervisor().getId(), e.getSupervisor().getName()))
						.distinct()
						.collect(Collectors.toList());
				return new ResponseEntity<List<SupervisorDTO>>(employees, HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
			}
		}

	  
	  
	

}
