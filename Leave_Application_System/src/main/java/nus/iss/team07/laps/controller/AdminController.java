package nus.iss.team07.laps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import nus.iss.team07.laps.dto.EmployeeLeaveDTO;
import nus.iss.team07.laps.dto.LeaveEntitlementForm;
import nus.iss.team07.laps.dto.SupervisorDTO;
import nus.iss.team07.laps.interfacemethods.EmployeeInterface;
import nus.iss.team07.laps.interfacemethods.HolidayInterface;
import nus.iss.team07.laps.interfacemethods.LeaveCountInterface;
import nus.iss.team07.laps.interfacemethods.LeaveTypeInterface;
import nus.iss.team07.laps.model.Department;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.Holidays;
import nus.iss.team07.laps.model.LeaveCount;
import nus.iss.team07.laps.model.LeaveType;
import nus.iss.team07.laps.model.Role;
import nus.iss.team07.laps.model.UserSession;
import nus.iss.team07.laps.service.DepartmentService;
import nus.iss.team07.laps.service.RoleService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/admin")
@SessionAttributes(value = { "usession" }, types = { UserSession.class })

public class AdminController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private LeaveCountInterface leaveCountService;

	@Autowired
	private EmployeeInterface employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private LeaveTypeInterface leaveTypeService;

	@Autowired
	private HolidayInterface holidayService;

	// Manage Holidays

	@GetMapping("/holidays")
	public String viewHolidays(Model model) {
		model.addAttribute("holidays", holidayService.findAllHolidays());
		return "Admin/displayholiday";
	}

	@GetMapping("/holidaysform")
	public String createHolidays(Model model) {
		model.addAttribute("holidays", new Holidays());
		return "Admin/holidayform";
	}

	@GetMapping("/holidayform/edit/{id}")
	public String editHolidayForm(@PathVariable("id") Integer id, Model model) {
		Holidays holidays = holidayService.findHolidayById(id).get();

		if (holidays == null) {
			return "redirect:/holidays"; // removed /admin
		}

		model.addAttribute("holidays", holidays);
		return "Admin/holidayform";
	}

	@PostMapping("/holidays/create")
	public String createLeaveType(@ModelAttribute("holidays") @Valid Holidays holiday, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("holidays", holiday);

			return "Admin/holidayform";
		}
		holidayService.saveHolidays(holiday);

		return "redirect:/holidays"; // removed /admin
	}

	@RequestMapping(value = "/holidays/delete/{id}")
	public String deleteHolidays(@PathVariable("id") Integer id, Model model) {
		Optional<Holidays> HolidayEntry = holidayService.findHolidayById(id);
		System.out.println(id);
		HolidayEntry.ifPresent(value -> holidayService.deleteHolidays(value));

		return "redirect:/holidays"; // removed /admin
	}

	// Manage Leave Type

	@GetMapping("/leave-types")
	public String viewLeaveTypes(Model model) {
		model.addAttribute("leaveTypes", leaveTypeService.getAllLeaveTypes());
		return "Admin/displayLeaveType";
	}

	@RequestMapping(value = "/leave-type/delete/{id}")
	public String deleteLeaveType(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		Optional<LeaveType> leaveType = leaveTypeService.getLeaveTypeById(id);
		List<LeaveType> leaveTypeList = leaveTypeService.getAllLeaveTypes();

		System.out.println(leaveTypeList.size());
		if (leaveType.isPresent() && leaveTypeList.size() > 1) {
			leaveTypeService.deleteLeaveType(leaveType.get());
			redirectAttributes.addFlashAttribute("Message", "Leave Type deleted successfully.");

		} else {
			redirectAttributes.addFlashAttribute("Message",
					"Cannot delete leave type. At least one leave type must exist.");
		}

		return "redirect:/leave-types";
	}

	@RequestMapping(value = "/leave-type/edit/{id}")
	public String editLeaveTypeForm(@PathVariable("id") Integer id, ModelMap model) {
		Optional<LeaveType> leaveType = leaveTypeService.getLeaveTypeById(id);
		leaveType.ifPresent(value -> model.addAttribute("leaveType", value));
		return "Admin/editLeaveType";
	}

	@RequestMapping(value = "/leave-type/save")
	public String saveLeaveType(@ModelAttribute("leaveType") @Valid LeaveType leaveType, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "/editLeaveType"; // removed /admin
		}

		leaveTypeService.saveLeaveType(leaveType);
		return "redirect:/leave-types"; // removed /admin
	}

	@RequestMapping(value = "/leave-type/new")
	public String createLeaveTypeForm(Model model) {
		model.addAttribute("leaveType", new LeaveType());
		return "Admin/createLeaveType";
	}

	@PostMapping("/leave-type/create")
	public String createLeaveType(@ModelAttribute("leaveType") @Valid LeaveType leaveType, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "Admin/createLeaveType";
		}

		leaveTypeService.saveLeaveType(leaveType);
		leaveCountService.createLeaveCountforNewLeaveType(leaveType);
		return "redirect:/leave-types"; // removed /admin
	}

	// Leave-enetitlemnts
	@GetMapping("/leave-entitlements/resetLeaveCount")
	public String resetLeaveCount(Model model) {
		leaveCountService.resetLeaveCountForNewYear();
		model.addAttribute("message", "Leave counts have been reset successfully.");
		return "redirect:/leave-entitlements"; // removed /admin
	}

	@GetMapping("/leave-entitlements")
	public String viewLeaveEntitlements(Model model) {
		List<LeaveCount> leaveCounts = leaveCountService.findAllLeaveCounts();
		List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes(); // Assuming this method exists
		Map<Integer, EmployeeLeaveDTO> employeeLeaveMap = new HashMap<>();

		for (LeaveCount leaveCount : leaveCounts) {
			int employeeId = leaveCount.getEmployee().getId();
			EmployeeLeaveDTO dto = employeeLeaveMap.getOrDefault(employeeId, new EmployeeLeaveDTO());
			dto.setEmployeeId(employeeId);
			dto.setEmployeeName(leaveCount.getEmployee().getName());
			dto.setDepartment(leaveCount.getEmployee().getDepartment().getName());
			dto.setRole(leaveCount.getEmployee().getRole().getType());
			if (dto.getLeaveBalances() == null) {
				dto.setLeaveBalances(new HashMap<>());
			}
			dto.getLeaveBalances().put(leaveCount.getLeaveType().getType(), leaveCount.getAvailableLeave());

			employeeLeaveMap.put(employeeId, dto);
		}

		List<EmployeeLeaveDTO> employeeLeaveList = new ArrayList<>(employeeLeaveMap.values());
		model.addAttribute("employeeLeaveList", employeeLeaveList);
		model.addAttribute("leaveTypes", leaveTypes); // Add leave types to the model

		// model.addAttribute("leaveCounts", leaveCountService.findAllLeaveCounts());
		return "Admin/viewLeaveEntitlements";
	}

	@GetMapping("/leave-entitlement/edit/{employeeId}")
	public String editLeaveEntitlementForm(@PathVariable int employeeId, Model model) {
		LeaveEntitlementForm form = new LeaveEntitlementForm();
		form.setEmployeeId(employeeId);

		List<LeaveCount> leaveCounts = leaveCountService.findLeaveCountsByEmployeeId(employeeId);
		List<LeaveEntitlementForm.LeaveCountDTO> leaveCountDTOs = leaveCounts.stream().map(leaveCount -> {
			LeaveEntitlementForm.LeaveCountDTO dto = new LeaveEntitlementForm.LeaveCountDTO();
			dto.setLeaveTypeId(leaveCount.getLeaveType().getId());
			dto.setLeaveType(leaveCount.getLeaveType().getType());
			dto.setTotalLeave(leaveCount.getTotalLeave());
			dto.setTakenLeave(leaveCount.getTakenLeave());
			return dto;
		}).collect(Collectors.toList());

		form.setLeaveCounts(leaveCountDTOs);
		model.addAttribute("leaveEntitlementForm", form);
		return "Admin/editLeaveEntitlement";
	}

	@PostMapping("/leave-entitlement/edit")
	public String processEditLeaveEntitlement(@ModelAttribute("leaveEntitlementForm") @Valid LeaveEntitlementForm form,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "Admin/editLeaveEntitlement";
		}

		for (LeaveEntitlementForm.LeaveCountDTO dto : form.getLeaveCounts()) {
			LeaveCount leaveCount = leaveCountService.findLeaveCountByEmployeeIdAndLeaveTypeId(form.getEmployeeId(),
					dto.getLeaveTypeId());

			leaveCount.setTotalLeave(dto.getTotalLeave() == null ? 0.0 : dto.getTotalLeave());
			leaveCount.setTakenLeave(dto.getTakenLeave() == null ? 0.0 : dto.getTakenLeave());
			leaveCountService.saveLeaveCount(leaveCount);
		}
		return "redirect:/leave-entitlements"; // removed /admin
	}

	@GetMapping("/leave-entitlement/search")
	public String searchLeaveEntitlements(@RequestParam String criteria, @RequestParam String keyword, Model model) {
		System.out.println(keyword);

		List<LeaveCount> leaveCounts = leaveCountService.searchLeaveEntitlements(criteria, keyword);
		List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes(); // Assuming this method exists
		Map<Integer, EmployeeLeaveDTO> employeeLeaveMap = new HashMap<>();

		for (LeaveCount leaveCount : leaveCounts) {
			int employeeId = leaveCount.getEmployee().getId();
			EmployeeLeaveDTO dto = employeeLeaveMap.getOrDefault(employeeId, new EmployeeLeaveDTO());
			dto.setEmployeeId(employeeId);
			dto.setEmployeeName(leaveCount.getEmployee().getName());
			dto.setDepartment(leaveCount.getEmployee().getDepartment().getName());
			dto.setRole(leaveCount.getEmployee().getRole().getType());
			if (dto.getLeaveBalances() == null) {
				dto.setLeaveBalances(new HashMap<>());
			}
			dto.getLeaveBalances().put(leaveCount.getLeaveType().getType(), leaveCount.getAvailableLeave());

			employeeLeaveMap.put(employeeId, dto);
		}

		List<EmployeeLeaveDTO> employeeLeaveList = new ArrayList<>(employeeLeaveMap.values());
		model.addAttribute("employeeLeaveList", employeeLeaveList);
		model.addAttribute("leaveTypes", leaveTypes); // Add leave types to the model

		if (employeeLeaveList.isEmpty()) {
			model.addAttribute("message", "No results found.");
		}

		return "Admin/viewLeaveEntitlements";
	}

	// Employee List

	@GetMapping("/employee")
	public String getSearchPage(Model model) {
		List<Employee> employees = employeeService.findAllEmployee();
		model.addAttribute("employees", employees);
		getRelatedShowBacks(model, employees);
		return "Admin/employee-list";
	}

	@GetMapping("/employee/searching")
	public String search(@RequestParam("keyword") String k, Model model) {
		if (k.isBlank() || k.isEmpty()) {
			return "redirect:/employee";
		}
		// retrieve searcedhEmployees
		List<Employee> searchEmployees = employeeService.findEmployeeByName(k);
		List<Employee> employees = employeeService.findAllEmployee();
		model.addAttribute("employees", searchEmployees);
		getRelatedShowBacks(model, employees);
		return "Admin/employee-list";
	}

	@RequestMapping(value = "/employee/delete/{id}")
	public String deleteEmployee(@ModelAttribute("employee") @PathVariable("id") Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			employeeService.deleteEmployeeById(id);
			leaveCountService.deleteLeaveRecordByEmployeeId(id);
			redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete employee: " + e.getMessage());
		}

		return "redirect:/employee";
	}

	@GetMapping("/employee/edit/{id}")
	public String editEmployee(@PathVariable("id") Integer id, Model model) {
		Employee employee = employeeService.findEmployeeById(id);
		if (employee == null) {
			return "redirect:/employee";
		}
		addDepartmentsAndSupervisorsToModel(model, employee);
		return "Admin/employee-create";
	}

	@GetMapping("/employee/create")
	public String addEmployee(Model model) {
		Employee employee = new Employee();
		addDepartmentsAndSupervisorsToModel(model, employee);
		return "Admin/employee-create";
	}

	@PostMapping(value = { "/employee/create", "/employee/edit/{id}" })
	public String saveOrUpdateEmployee(@ModelAttribute("employee") @Valid Employee employee,
			BindingResult bindingResult, Model model,
			@RequestParam(name = "department", required = false) Integer departmentId,
			@RequestParam(name = "supervisor.id", required = false) Integer supervisorId,
			@RequestParam(name = "role.id", required = false) Integer roleId,
			@PathVariable(name = "id", required = false) Integer id) {

		if (bindingResult.hasErrors()) {
			addDepartmentsAndSupervisorsToModel(model, employee);
			return "Admin/employee-create";
		}

		// retrieve and set Department
		if (departmentId != null) {
			Department department = departmentService.findDepartmentById(departmentId);
			if (department == null) {
				bindingResult.rejectValue("department", "error.employee", "Invalid department ID.");
				addDepartmentsAndSupervisorsToModel(model, employee);
				return "Admin/employee-create";
			}
			employee.setDepartment(department);
		}

		// retrieve and set Supervisor
		if (supervisorId != null) {
			Employee supervisor = employeeService.findEmployeeById(supervisorId);
			// if can not find supervisor
			if (supervisor == null) {
				bindingResult.rejectValue("supervisor", "error.employee", "Invalid supervisor ID.");
				addDepartmentsAndSupervisorsToModel(model, employee);
				return "Admin/employee-create";
			}
			employee.setSupervisor(supervisor);
		}
		// Set join date if it's a new employee
		if (id == null) {
			employee.setJoin_date(LocalDate.now());
		}

		// Save or update employee
		if (id == null) {
			employeeService.saveEmployee(employee);
			leaveCountService.createCountforNewEmp(employee);
		} else {
			employeeService.updateEmployee(id, employee);
		}
		return "redirect:/employee";
	}

	// Role List
	@GetMapping("/roles")
	public String viewRoles(Model model) {
		model.addAttribute("roles", roleService.findAllRoles());
		return "Admin/role-list";
	}

	@GetMapping("/role/edit/{id}")
	public String editRole(@PathVariable("id") Integer id, Model model) {
		Role role = roleService.findRoleById(id);
		if (role == null) {
			// Handle role not found, redirect or show error
			model.addAttribute("errorMessage", "Sorry~ Role not found, please try again.");
			return "check-error";
		}
		model.addAttribute("role", role);
		return "Admin/role-create";
	}

	@GetMapping("/role/create")
	public String addRole(Model model) {
		Role role = new Role();
		model.addAttribute("role", role);
		return "Admin/role-create";
	}

	// role/create to role/form
	@RequestMapping(value = { "/role/create", "/role/edit/{id}" })
	public String saveOrUpdateRole(@PathVariable(name = "id", required = false) Integer id,
			@ModelAttribute("role") @Valid Role role, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "Admin/role-create";
		}

		// Save or update role
		if (id == null) {
			roleService.saveRole(role);
		} else {
			roleService.updateRole(id, role);
		}
		return "redirect:/roles";
	}

	@RequestMapping(value = "/role/delete/{id}")
	public String deleteRole(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		boolean hasEmployees = roleService.hasEmployeesWithRole(id);

		if (hasEmployees) {
			String errorMessage = "Please remove all employee roles associated <br> with this role before deleting them~";

			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			return "redirect:/roles"; 
		}
		roleService.deleteRoleById(id);
		return "redirect:/roles"; 
	}

	// Department List
	@GetMapping("/departments")
	public String viewDepartments(Model model) {
		model.addAttribute("departments", departmentService.findAllDepartments());
		return "Admin/department-list";
	}

	@GetMapping("/department/edit/{id}")
	public String editDepartment(@PathVariable("id") Integer id, Model model) {
		Department department = departmentService.findDepartmentById(id);
		if (department == null) {
			// Handle department not found, redirect or show error
			model.addAttribute("errorMessage", "Sorry~ Department not found, please try again.");
			return "check-error";
		}
		model.addAttribute("department", department);
		return "Admin/department-create";
	}

	@GetMapping("/department/create")
	public String addDepartment(Model model) {
		Department department = new Department();
		model.addAttribute("department", department);
		return "Admin/department-create";
	}

	// change create to form
	@RequestMapping(value = { "/department/create", "/department/edit/{id}" })
	public String saveOrUpdateDepartment(@PathVariable(name = "id", required = false) Integer id,
			@ModelAttribute("department") @Valid Department department, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "Admin/department-create";
		}

		if (id == null) {
			departmentService.saveDepartment(department);
		} else {
			departmentService.updateDepartment(id, department);
		}
		return "redirect:/departments";
	}

	@RequestMapping(value = "/department/delete/{id}")
	public String deleteDepartment(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		boolean hasEmployees = departmentService.hasEmployeesWithDepartment(id);

		if (hasEmployees) {
			String errorMessage = "Please remove all employee roles associated <br> with this department before deleting them~";
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			// return "check-error";
			return "redirect:/departments"; 

		}
		departmentService.deleteDepartmentById(id);
		return "redirect:/departments";
	}

	private void addDepartmentsAndSupervisorsToModel(Model model, Employee employee) {
		List<Department> departments = departmentService.findAllDepartments();
		// set default department when creating new employee
		if (!departments.isEmpty() && employee.getDepartment() == null) {
			Department defaultDepartment = departments.get(0);
			employee.setDepartment(defaultDepartment);
			List<Employee> supervisors = departmentService.findSupervisorsByDepartment(defaultDepartment);
			model.addAttribute("supervisors", supervisors);
		}
		// retrieve all roles
		List<Role> roles = roleService.findAllRoles();
		// LocalDate is cause the problem. use SupervisorDTO only retrieve the id and name
		Map<Integer, List<SupervisorDTO>> departmentSupervisors = new HashMap<>();
		Map<Integer, List<SupervisorDTO>> departmentemployees = new HashMap<>();

		for (Department department : departments) {
			List<Employee> flEmployees = departmentService.findSupervisorsByDepartment(department);
			// Map to SupervisorDTO
			List<SupervisorDTO> employeesByDepartment = departmentService.findEmployeesByDepartment(department).stream()
					.filter(e -> !e.equals(employee)).map(e -> new SupervisorDTO(e.getId(), e.getName())) 
					.collect(Collectors.toList());
			// Map to SupervisorDTO
			List<SupervisorDTO> supervisors = flEmployees.stream().filter(e -> !e.equals(employee))
					.map(e -> new SupervisorDTO(e.getId(), e.getName())) 
					.collect(Collectors.toList());
			departmentSupervisors.put(department.getId(), supervisors);
			departmentemployees.put(department.getId(), employeesByDepartment);
		}

		// retrieve supervisors exclude self
		model.addAttribute("departments", departments);
		model.addAttribute("roles", roles);

		// Serialize the map to JSON
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String departmentSupervisorsJson = objectMapper.writeValueAsString(departmentSupervisors);
			String departmentEmployeesJson = objectMapper.writeValueAsString(departmentemployees);
			model.addAttribute("departmentSupervisorsJson", departmentSupervisorsJson);
			model.addAttribute("departmentEmployeesJson", departmentEmployeesJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("departmentSupervisorsJson", "{}"); // Provide a default empty JSON object
			model.addAttribute("departmentEmployeesJson", "{}"); // Provide a default empty JSON object
		}
		model.addAttribute("employee", employee);
	}

	private void getRelatedShowBacks(Model model, List<Employee> employees) {
		List<Department> departments = departmentService.findAllDepartments();
		List<Role> roles = roleService.findAllRoles();
		// get all supervisors
		List<Employee> supervisors = employeeService.findAllSupervisors();
		model.addAttribute("departments", departments);
		model.addAttribute("supervisors", supervisors);
		model.addAttribute("roles", roles);
	}

}