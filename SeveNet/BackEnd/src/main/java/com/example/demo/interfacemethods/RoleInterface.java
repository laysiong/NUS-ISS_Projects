package com.example.demo.interfacemethods;

import java.util.List;

import com.example.demo.model.Role;

public interface RoleInterface {
	
	List<Role> findAllRoles();
	Role findRoleById(Integer id);
	Role findRoleByType(String type);
	
	Role saveRole(Role role);
	void deleteRoleById(Integer id);
	Role updateRole(Integer id, Role newRole);
	
}
