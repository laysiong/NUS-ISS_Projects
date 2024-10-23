package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.DuplicateRoleTypeException;
import com.example.demo.model.Role;
import com.example.demo.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Role>> getAllRole() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(roleService.findRoleById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.saveRole(role));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Integer id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DuplicateRoleTypeException.class)
    public ResponseEntity<String> handleDuplicateRoleTypeException(DuplicateRoleTypeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
