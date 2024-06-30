package nus.iss.team07.laps.interfacemethods;

import java.util.Collection;
import java.util.List;

import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.Holidays;

public interface EmployeeInterface {
	
	List<Employee> findAllEmployee();
	boolean saveEmployee(Employee employee);
	Employee updateEmployee(Integer id, Employee employee);
	List<Employee> findEmployeeByName(String name);
	List<Employee> findEmployeeByRole(String role);
	Employee findEmployeeById(Integer id);	
	void deleteEmployeeById(Integer id);
	List<Employee> findAllSupervisors();
	List<Employee> getEmployeeSupervisor(int id);
	
	Employee findEmployee (Integer userId);
//    List<String> getUserRoles(Integer userId);
	//Check user
	Employee authenicate (String username, String pwd);
	List<Employee> getEmpUnderUs(int id, String keyword);
	List<Employee> findSupervisorByDepartmentAndRole(List<Integer> departmentId,List<Integer> roleId);

	
	
	
	
}
