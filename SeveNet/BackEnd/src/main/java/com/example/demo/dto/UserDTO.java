package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.User;
import com.example.demo.statusEnum.UserStatus;

public class UserDTO {
	
	// for return type of block Users 

	private Integer id;

	private String name;

	private String email;

	private String username;

	private String gender;

	private String country;

	private UserStatus status;
	
	private String phoneNum;
	
	private LocalDate joinDate;
	
	// Constructor, getters, and setters
	public UserDTO (User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.gender = user.getGender();
		this.country = user.getCountry();
		this.status = user.getStatus();
		this.phoneNum = user.getPhoneNum();
		this.joinDate = user.getJoinDate();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}
	
}
