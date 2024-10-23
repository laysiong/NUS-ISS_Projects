package nus.iss.team07.laps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nus.iss.team07.laps.model.Department;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.Holidays;
import nus.iss.team07.laps.repository.DepartmentRepository;
import nus.iss.team07.laps.repository.EmployeeRepository;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department findDepartmentById(Integer id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = false)
    public void deleteDepartmentById(Integer id) {
        departmentRepository.deleteById(id);
    }
    
    public Department findDepartmentByName(String name) {
    	return departmentRepository.findByName(name);
    }
    
    @Transactional(readOnly = false)
    public Department updateDepartment(Integer id, Department department) {
    	Department existingD = departmentRepository.findById(id).get();
    	existingD.setContact(department.getContact());
    	existingD.setFloor(department.getFloor());
    	existingD.setName(department.getName());
        return existingD;
    }
    
    public List<Employee> findSupervisorsByDepartment(Department department) {
        // Implement the logic of querying the list of supervisors 
    	// for specified departments in the database
        return departmentRepository.findSupervisors(department.getId());
    }
    
    public List<Employee> findEmployeesByDepartment(Department department) {
        // Implement the logic of querying the list of supervisors 
    	// for specified departments in the database
        return departmentRepository.findEmployees(department.getId());
    }
    
    public boolean hasEmployeesWithDepartment(Integer id) {
    	List<Employee> employees = employeeRepository.findByDepartmentId(id);
        return !employees.isEmpty();
    }

}