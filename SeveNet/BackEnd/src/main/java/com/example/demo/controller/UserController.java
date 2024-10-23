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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.exception.DuplicateUserException;
import com.example.demo.exception.DuplicateTypeException;
import com.example.demo.interfacemethods.FollowInterface;
import com.example.demo.interfacemethods.UserInterface;
import com.example.demo.model.FollowList;
import com.example.demo.model.User;
import com.example.demo.statusEnum.UserStatus;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserInterface userService;
    
    @Autowired
    private FollowInterface followService;

    // check user_name and password if match then login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest ) {
        User user = userService.authenicate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (DuplicateTypeException e) {
        	return ResponseEntity.status(409).build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/findAllBlockUserByUId/{userId}")
    public ResponseEntity<List<UserDTO>> getAllBlockUsersByUId(@PathVariable("userId") Integer userId) {
        List<UserDTO> blockUsers = userService.findAllBlockUsersByUId(userId);
        return ResponseEntity.ok(blockUsers);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable("id") Integer id) {
        User user = userService.findUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable("name") String name) {
        List<User> users = userService.findUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findByRole/{role}")
    public ResponseEntity<List<User>> getUserByRole(@PathVariable("role") String role) {
        List<User> users = userService.findUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<List<User>> saveUser(@RequestBody User user) {
    	userService.saveUser(user);
        List<User> employees = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(employees);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<List<User>> updateUser(@PathVariable("id") Integer id, @RequestBody User updateUser) {
        userService.updateUser(id, updateUser);
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Void> updateUserStatus(@PathVariable("id") Integer id, @RequestParam("status") UserStatus status) {
        userService.updateUserStatusById(id, status);
        return ResponseEntity.noContent().build();
    }
    
    // the following two API are for the updating socialScores
    // the adjustScore is the one to be added or minus from the current score
    // add user social score
    @PutMapping("/addSocialScore/{id}")
    public ResponseEntity<Void> addUserSocialScore(@PathVariable("id") Integer id, @RequestParam("score") Integer adjustScore) {
    	userService.updateUserSocialScoresById(id, "add",adjustScore);
    	return ResponseEntity.noContent().build();
    }
    
    // minus user social score
    @PutMapping("/minusSocialScore/{id}")
    public ResponseEntity<Void> minusUserSocialScore(@PathVariable("id") Integer id, @RequestParam("score") Integer adjustScore) {
        userService.updateUserSocialScoresById(id, "minus",adjustScore);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("{UserId}/block/{blockId}")
    public ResponseEntity<Void> blockUser(@PathVariable("UserId") Integer UserId, @PathVariable("blockId") Integer blockId) {
    	userService.blockUserById(UserId,blockId);
    	return ResponseEntity.noContent().build();
    }
    
    @PutMapping("{UserId}/unblock/{unblockId}")
    public ResponseEntity<Void> unblockUser(@PathVariable("UserId") Integer UserId, @PathVariable("unblockId") Integer unblockId) {
        userService.unblockUserById(UserId, unblockId);
        return ResponseEntity.noContent().build();
    }

    
    
    @PostMapping("/{followerId}/follow/{followedUserId}")
    public ResponseEntity<String> followUser(@PathVariable Integer followerId, @PathVariable Integer followedUserId) {
        User follower = userService.findUserById(followerId);
        User followedUser = userService.findUserById(followedUserId);
        followService.followUser(follower, followedUser);
        return ResponseEntity.ok("User followed successfully.");
    }

    @DeleteMapping("/{followerId}/unfollow/{followedUserId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Integer followerId, @PathVariable Integer followedUserId) {
        User follower = userService.findUserById(followerId);
        User followedUser = userService.findUserById(followedUserId);
        followService.unfollowUser(follower, followedUser);
        return ResponseEntity.ok("User unfollowed successfully.");
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Integer userId) {
        User user = userService.findUserById(userId);
        List<User> followers = followService.getFollowers(user);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<User>> getFollowings(@PathVariable Integer userId) {
        User user = userService.findUserById(userId);
        List<User> followings = followService.getFollowings(user);
        return ResponseEntity.ok(followings);
    }

    @GetMapping("/{userId}/followersCount")
    public ResponseEntity<Integer> getFollowersCount(@PathVariable Integer userId) {
        User user = userService.findUserById(userId);
        Integer followers = followService.getFollowers(user).size();
        System.out.println(followers);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/followingsCount")
    public ResponseEntity<Integer> getFollowingsCount(@PathVariable Integer userId) {
        User user = userService.findUserById(userId);
        Integer followings = followService.getFollowings(user).size();
        System.out.println(followings);
        return ResponseEntity.ok(followings);
    }
    
    @GetMapping("/{curId}/isfollower/{otherId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Integer curId, @PathVariable Integer otherId) {
        User CurId = userService.findUserById(curId);
        User OtherId = userService.findUserById(otherId);
        boolean followings = followService.isFollowing(CurId,OtherId);
        return ResponseEntity.ok(followings);
    }
    
    
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
