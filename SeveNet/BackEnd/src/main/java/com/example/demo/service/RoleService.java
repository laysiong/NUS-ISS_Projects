package com.example.demo.service;

import java.util.List;

import com.example.demo.configuration.WebSocketUserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.DuplicateRoleTypeException;
import com.example.demo.interfacemethods.RoleInterface;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;

@Service
@Transactional(readOnly = true)
public class RoleService implements RoleInterface {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WebSocketUserHandler webSocketUserHandler;

	@Override
	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role findRoleById(Integer id) {
		return roleRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Role not found with ID" + id));
	}
	
	@Override
	public Role findRoleByType(String type) {
		try {
			return roleRepository.findByTypeIgnoreCase(type);
		} catch (Exception e) {
			throw new RuntimeException("Failed to find Role with type: " + type, e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public Role saveRole(Role role) {
		// check if have same type cannot create
		handleDuplicateRole(role);
        return roleRepository.save(role);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRoleById(Integer id) {
		try {
			roleRepository.deleteById(id);
			webSocketUserHandler.sendAllUserUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete Role with ID: " + id, e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public Role updateRole(Integer id, Role newRole) {
		Role existingRole = findRoleById(id);
		existingRole.setType(newRole.getType());
		webSocketUserHandler.sendAllUserUpdate();
		return roleRepository.save(existingRole);
	}

	private void handleDuplicateRole(Role role) {
		if (roleRepository.findByTypeIgnoreCase(role.getType()) != null) {
            throw new DuplicateRoleTypeException("Role type already exists: " + role.getType());
        }
	}
}
