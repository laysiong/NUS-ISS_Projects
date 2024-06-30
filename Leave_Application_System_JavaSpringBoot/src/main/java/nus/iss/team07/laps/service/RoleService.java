package nus.iss.team07.laps.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.Role;
import nus.iss.team07.laps.repository.RoleRepository;
import nus.iss.team07.laps.repository.EmployeeRepository;

@Service
@Transactional(readOnly = true)
public class RoleService {
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	
	

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
    
    public Role findRoleById(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }
    
    public Role findRoleByType(String type) {
        return roleRepository.findByType(type);
    }

    @Transactional(readOnly = false)
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Transactional(readOnly = false)
    public Role updateRole(Integer id, Role role) {
        Role existingRole = roleRepository.findById(id).get();
        existingRole.setType(role.getType());
        return existingRole;
    }

    @Transactional(readOnly = false)
    public void deleteRoleById(Integer id) {
        roleRepository.deleteById(id);
    }
    

    public boolean hasEmployeesWithRole(Integer roleId) {

	  Boolean result = employeeRepository.hasEmployeesWithThisRole(roleId);
	  //System.out.println("Employees with role ID " + roleId + result);  // Add logging

	  return result != null && result;
    }

}

