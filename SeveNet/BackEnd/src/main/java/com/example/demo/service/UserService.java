package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.demo.configuration.WebSocketUserHandler;
import com.example.demo.dto.EmailRequest;
import com.example.demo.interfacemethods.NotificationInterface;
import com.example.demo.model.Notification;
import com.example.demo.statusEnum.NotificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.DuplicateUserException;
import com.example.demo.interfacemethods.UserInterface;
import com.example.demo.model.Auth;
import com.example.demo.model.User;
import com.example.demo.statusEnum.UserStatus;
import com.example.demo.model.Role;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.FollowListRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PasswordUtil;

@Service      
@Transactional(readOnly = true)
public class UserService implements UserInterface {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthRepository authRepository;

    @Autowired 
    private FollowListRepository followListRepository;

	@Autowired
	private WebSocketUserHandler webSocketUserHandler;

	@Autowired
	private NotificationInterface notificationService;

	@Autowired
	private EmailService emailService;
	
	//Count number of public users
	public Integer CountUsers() {
		return userRepository.countUsers();
	}

	// can not save or update the same employee/user
	// use this method to handleDuplicateEmployee and throw exceptions accordingly 
	private void handleDuplicateUser(User user) {
		// check user_name & email
		// find User by user_name
		List<User> findUserList = userRepository.findByUserNameIgnoreCase(user.getUsername());
		if (!findUserList.isEmpty()) {
			List<String> emailList = findUserList.stream().map(f -> f.getEmail().toLowerCase()).distinct()
					.toList();
			// if same user_name and same email, we consider it the same user
			if (emailList.contains(user.getEmail().toLowerCase())) {
				// save fail
				throw new DuplicateUserException("User already exists: " + user.getUsername());
			}
		}
	}
	
	private void changeUserStatusById(Integer id, UserStatus operation) {
		try {
			User curUser = findUserById(id);
			curUser.setStatus(operation);
			userRepository.save(curUser);
		} catch (Exception e) {
			throw new RuntimeException("Failed to " + operation + " user with ID: " + id, e);
		}
	}

	// used for user log in
	@Override
	public User authenicate(String username, String pwd) {
		User checkUser = userRepository.findByUserName(username);
        if (checkUser != null) {
            // check password
        	boolean isMathched = false;
        	try {
        		isMathched = PasswordUtil.decryptPassword(checkUser.getPassword()).equals(PasswordUtil.decryptPassword(pwd));
        	} catch (Exception e) {
    			throw new RuntimeException("Failed to decrypt pwd with user " + username, e);
    		}
        	if(isMathched) {
        		// check user status if not active we don't allow this user log in
        		if (checkUser.getStatus().equals(UserStatus.active)) {
                	return checkUser;	
        		} else {
        			throw new RuntimeException("No longer active user!");
        		}
            } else {
            	throw new RuntimeException("Wrong username or pwd!");
            }
            
        } else {
			throw new RuntimeException("Not found user with name: " + username);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public User registerUser(User user) {
		// Check if the user_name already exists
		if (userRepository.findByUserName(user.getUsername()) != null) {
			throw new DuplicateUserException("User Name already exists.");
		}

		// Encrypt the password before saving (do it at front end	)
		// can do the encryption both back end & front end
		// employee.setPassword(PasswordUtil.encodePassword(employee.getPassword()));

		Role defaultRole = roleRepository.findByTypeIgnoreCase("User");
		List<Auth> allAuth = authRepository.findAll();

		// Set the default role and authorization for new employees/users
		// here we set their auth to "FixedRankForSocialScore:111-120"

		Auth defaultAuth;
		if (!allAuth.isEmpty()) {
			// what will happen if there's no role or authorization? maybe when JUnit test we will add auth
			// set the first authorization as default authorization ?
			defaultAuth = authRepository.findByRank("FixedRankForSocialScore:91-120");
		} else {
			defaultAuth = null;
		}
		
		user.setRole(defaultRole);
		user.setAuth(defaultAuth);
		// set the register date as the join date
		user.setJoinDate(LocalDate.now());
		// set the default socialScore to 120
		user.setSocialScore(120);
		// set the default status to 'active'
		user.setStatus(UserStatus.active);
		return userRepository.save(user);
	}

	@Override
	public List<User> findAllUsers() {
		// only return users whose status is not "delete"
		return userRepository.findAll().stream().filter(u->!u.getStatus().equals(UserStatus.delete)).toList();
	}

	@Override
	public List<User> findUsersByName(String name) {
		return userRepository.findByName(name);
	}

	@Override
	public User findByUserName(String name) {
		return userRepository.findByUserName(name);
	}

	@Override
	public List<User> findUsersByRole(String role) {
		return userRepository.findhByJobRole(role);
	}

	@Override
	public User findUserById(Integer id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with ID:" + id));
	}
	
	@Override
	public List<UserDTO> findAllBlockUsersByUId(Integer UserId) {
		User curUser = findUserById(UserId);
		String blockList = curUser.getBlockList();

        if (blockList == null || blockList.isEmpty()) {
        	// if haven't block yet
        	return new ArrayList<>(); // return an empty list instead of null
        } else {
        	List<String> blockIds = new ArrayList<>(Arrays.asList(blockList.split(",")));

            return  blockIds.stream()
					.map(blockId -> {
						User blockedUser = findUserById(Integer.parseInt(blockId));
						return new UserDTO(blockedUser);
					})
					.collect(Collectors.toList());
        }
	}

	@Override
	@Transactional(readOnly = false)
	public User saveUser(User user) {
		handleDuplicateUser(user);
		return userRepository.save(user);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateUserStatusById(Integer id, UserStatus status) {
		changeUserStatusById(id,status);
		webSocketUserHandler.sendUserUpdate(id);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateUserSocialScoresById(Integer id, String operation,Integer adjustScore) {
		try {
			User curUser = findUserById(id);
			Integer curScore = curUser.getSocialScore();
			if (operation.equals("add")) {
				curScore+=adjustScore;
			} else if (operation.equals("minus")) {
				curScore-=adjustScore;
			}
			if (curScore > 120) curScore = 120;
			if (curScore < 0) curScore = 0;
			curUser.setSocialScore(curScore);
			userRepository.save(curUser);
			webSocketUserHandler.sendUserUpdate(id);
		} catch (Exception e) {
			throw new RuntimeException("Failed to " + operation + " user socialScore with ID: " + id, e);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void blockUserById(Integer userId, Integer blockUserId) {

		// Can not block him/herself
		if (Objects.equals(userId, blockUserId)) {
			throw new RuntimeException("User cannot block him/herself");
		}

		User curUser = findUserById(userId);
		findUserById(blockUserId);

		String blockList = curUser.getBlockList();
		List<String> blockIds;

		if (blockList == null || blockList.isEmpty()) {
			blockIds = new ArrayList<>();
		} else {
			blockIds = new ArrayList<>(Arrays.asList(blockList.split(",")));
		}

		if (blockIds.contains(blockUserId.toString())) {
			blockIds.remove(blockUserId.toString());
		} else {
			blockIds.add(blockUserId.toString());
		}

		curUser.setBlockList(String.join(",", blockIds));
		userRepository.save(curUser);
		webSocketUserHandler.sendUserUpdate(userId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void unblockUserById(Integer UserId, Integer unblockUserId) {
		User curUser = findUserById(UserId);
		findUserById(unblockUserId);

		String blockList = curUser.getBlockList();
	   	if (blockList != null && !blockList.isEmpty()) {
	   		List<String> blockIds = new ArrayList<>(Arrays.asList(blockList.split(",")));
	   		blockIds.remove(unblockUserId.toString());
	    	blockList = String.join(",", blockIds);
	    	curUser.setBlockList(blockList);
	    	userRepository.save(curUser);
	   	}
	}
	
	
	@Override
	@Transactional(readOnly = false)
	public User updateUser(Integer id, User user) {
		User curUser = findUserById(id);
		// we do all the updates here and 
		// let the controller do the logic to choose
		// whatever to update.
		curUser.setName(user.getName());
		curUser.setEmail(user.getEmail());
		curUser.setPassword(user.getPassword());
		curUser.setUsername(user.getUsername());
		curUser.setPhoneNum(user.getPhoneNum());
		curUser.setGender(user.getGender());
		curUser.setCountry(user.getCountry());
		curUser.setBlockList(user.getBlockList());
		curUser.setSocialScore(user.getSocialScore());
		if (!curUser.getAuth().getId().equals(user.getAuth().getId())) {
			// if auth been updated, sen notification to the user
			String notifTitle = "Authorization updated";
			String notifMsg = "This is a System-Auto-Generated-Notification: \n" +
					"Your Authorization has been updated by the moderator.";
			LocalDateTime notifDateTime = LocalDateTime.now();
			Notification systemGeneratedNotif = new Notification(curUser,notifTitle,notifMsg,notifDateTime, NotificationStatus.Unread);
			notificationService.saveNotification(systemGeneratedNotif);
		}
		curUser.setAuth(user.getAuth());
		curUser.setStatus(user.getStatus());
		curUser.setRole(user.getRole());
		curUser.setJoinDate(user.getJoinDate());
		if (user.getStatus().equals(UserStatus.ban)) {
			// if this user's status been changed to 'ban'
			// ban this user & send email
			String systemAutoBanEmialSubject = "Ban Email";
			String systemAutoBanEmialText = "We're very sorry to inform you that you've been baned by the moderator.";
			EmailRequest systemAutoBanEmial = new EmailRequest(curUser.getEmail(),systemAutoBanEmialSubject,systemAutoBanEmialText);
			try {
				emailService.sendSimpleMessage(systemAutoBanEmial.getTo(), systemAutoBanEmial.getSubject(), systemAutoBanEmial.getText());
			} catch (Exception e) {
				throw new RuntimeException("Failed to send email\n"+e.getMessage());
			}
		}
		webSocketUserHandler.sendAllUserUpdate();
		return userRepository.save(curUser);
	}
	
	@Override
	@Transactional(readOnly = false)
	public Auth checkUserSocialScoreThenAdjustAuth(Integer userId) {
		User curUser = findUserById(userId);
		Integer curSocialScore = Math.max(curUser.getSocialScore(),0);
		int curRank = (curSocialScore + 9) / 10;
		// set socialScore 1-60 as a list for judgment
		List<Integer> fistRandList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
		// set socialScore 91-120 as a list for judgment
		List<Integer> secondRankList = new ArrayList<Integer>(Arrays.asList(10,11,12));
		// default set format of "FixedRankForSocialScore:1-60" "FixedRankForSocialScore:0"
		Auth adjustAuth;
		if (fistRandList.contains(curRank)) {
			adjustAuth = authRepository.findByRank("FixedRankForSocialScore:1-60");
		} else if (secondRankList.contains(curRank)){
			adjustAuth = authRepository.findByRank("FixedRankForSocialScore:91-120");
		} else if (curRank == 0) {
			// we should ban this user
			// but here we just return the authorization
			adjustAuth = authRepository.findByRank("FixedRankForSocialScore:0");
		} else {
			adjustAuth = authRepository.findByRank("FixedRankForSocialScore:" + ((curRank - 1) * 10 + 1) + "-" + curRank * 10);
		}
		curUser.setAuth(adjustAuth);
		userRepository.save(curUser);
		webSocketUserHandler.sendUserUpdate(userId);
		return adjustAuth;
	}

}