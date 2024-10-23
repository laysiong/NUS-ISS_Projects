package com.example.demo.interfacemethods;

import java.util.List;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Auth;
import com.example.demo.model.User;
import com.example.demo.statusEnum.UserStatus;

public interface UserInterface {
	
	Integer CountUsers();

	List<User> findAllUsers();
	List<User> findUsersByName(String name);
	List<User> findUsersByRole(String role);
	User findUserById(Integer id);
	User findByUserName(String name);
	
	User saveUser(User user);
	void updateUserStatusById(Integer id, UserStatus status);
	void updateUserSocialScoresById(Integer id, String operation, Integer adjustScore);
	void blockUserById(Integer UserId, Integer blockId);
	void unblockUserById(Integer UserId, Integer unblockId);
	List<UserDTO> findAllBlockUsersByUId(Integer UserId);
	User updateUser(Integer id, User user);

	//Check user
	User authenicate (String username, String pwd);
	User registerUser(User user);
	Auth checkUserSocialScoreThenAdjustAuth(Integer userId);
}
