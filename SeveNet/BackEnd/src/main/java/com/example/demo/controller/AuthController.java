package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.DuplicateAuthException;

import com.example.demo.exception.DuplicateTypeException;
import com.example.demo.interfacemethods.AuthInterface;
import com.example.demo.model.Auth;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthInterface authService;
    
    @GetMapping("/findAll")
    public ResponseEntity<List<Auth>> getAllAuth() {
        List<Auth> auths = authService.findAllAuths();
        return ResponseEntity.ok(auths);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Auth> getAuthById(@PathVariable("id") Integer id) {
        Auth auth = authService.findAuthById(id);
        if (auth != null) {
            return ResponseEntity.ok(auth);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findByRank/{rank}")
    public ResponseEntity<Auth> getAuthByRank(@PathVariable("rank") String rank) {
        Auth auth = authService.findAuthByRank(rank);
        if (auth != null) {
            return ResponseEntity.ok(auth);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // after user login get auth and perform menu and url block etc.
    @GetMapping("/findByUserId/{userId}")
    public ResponseEntity<Auth> getAuthByuserId(@PathVariable("userId") Integer userId) {
        // 
        Auth userAuth = authService.findAuthByUserId(userId);

        if (userAuth != null) {
            return ResponseEntity.ok(userAuth);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<List<Auth>> saveAuth(@RequestBody Auth auth) {
        try {
            authService.saveAuth(auth);
            List<Auth> auths = authService.findAllAuths();
            return ResponseEntity.ok(auths);
        } catch (DuplicateTypeException e) {
            return ResponseEntity.status(409).build();  // 409 Conflict
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<List<Auth>> updateAuth(@PathVariable("id") Integer id, @RequestBody Auth updateAuth) {
        authService.updateAuth(id, updateAuth);
        List<Auth> auths = authService.findAllAuths();
        return ResponseEntity.ok(auths);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuth(@PathVariable("id") Integer id) {
        authService.deleteAuthById(id);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(DuplicateAuthException.class)
    public ResponseEntity<String> handleDuplicateAuthException(DuplicateAuthException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
