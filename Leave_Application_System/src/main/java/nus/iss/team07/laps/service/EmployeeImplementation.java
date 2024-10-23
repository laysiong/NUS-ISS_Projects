package nus.iss.team07.laps.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nus.iss.team07.laps.interfacemethods.EmployeeInterface;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.repository.EmployeeRepository;
import nus.iss.team07.laps.repository.LeaveCountRepository;
import nus.iss.team07.laps.repository.LeaveTypeRepository;
import nus.iss.team07.laps.repository.RoleRepository;

@Service
public class EmployeeImplementation implements EmployeeInterface{
   
	@Autowired
	private EmployeeRepository employeeRepository;
	    
	@Autowired
	private  RoleRepository roleRepository;
	
	@Transactional
	@Override
	public Employee findEmployee(Integer userId) {
		return employeeRepository.findById(userId).orElse(null);
	}
	
	@Override
	public Employee authenicate(String username, String pwd) {
		return employeeRepository.findUserByNamePwd(username,pwd);
	}

//	public List<String> getUserRoles(Integer integer) {
//		return employeeRepository.findByRoleId(integer);
//	}
	
	/* newer add */
	
	@Override
	public List<Employee> findAllEmployee() {
		return employeeRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean saveEmployee(Employee employee) {
		// TODO Auto-generated method stub
		if (employeeRepository.save(employee) != null)
			return true;
		else
			return false;
	}
	
	@Override
	public List<Employee> findEmployeeByName(String name) {
		return employeeRepository.SearchEmployeeByName(name);
	}

	@Override
	public List<Employee> findEmployeeByRole(String role) {
		return employeeRepository.SearchEmployeeByJobRole(role);
	}
	
	@Override
	public Employee findEmployeeById(Integer id) {
		// TODO Auto-generated method stub
		return employeeRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteEmployeeById(Integer id) {
		try {
			employeeRepository.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete employee with ID: " + id, e);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public Employee updateEmployee(Integer id, Employee employee) {
		Employee curEmployee = employeeRepository.findById(id).get();
		curEmployee.setDepartment(employee.getDepartment());
		curEmployee.setEmail(employee.getEmail());
		curEmployee.setRole(employee.getRole());
		curEmployee.setPassword(employee.getPassword());
		curEmployee.setSupervisor(employee.getSupervisor());
		curEmployee.setDepartment(employee.getDepartment());
		curEmployee.setUsername(employee.getUsername());
		curEmployee.setContactNum(employee.getContactNum());
		curEmployee.setName(employee.getName());
		return curEmployee;
	}
	
	@Override
	public List<Employee> getEmployeeSupervisor(int id) {
		return employeeRepository.getEmployeeSupervisor(id);
	}

	@Override
	public List<Employee> findAllSupervisors() {
		// TODO Auto-generated method stub
		return employeeRepository.findAllSupervisors();
	}
	
	@Override
	public List<Employee> getEmpUnderUs(int id, String keyword) {
		return employeeRepository.searchEmpUnderUs(id,keyword);
	} 
	
	@Override
	public List<Employee> findSupervisorByDepartmentAndRole(List<Integer> departmentId, List<Integer> roleId) {
		return employeeRepository.getSupervisorByDepartmentAndRole(departmentId, roleId);
	} 
}
