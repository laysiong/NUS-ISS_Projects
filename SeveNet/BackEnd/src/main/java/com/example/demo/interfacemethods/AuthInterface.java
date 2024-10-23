package com.example.demo.interfacemethods;

import java.util.List;

import com.example.demo.model.Auth;

public interface AuthInterface {
	List<Auth> findAllAuths();
	Auth findAuthById(Integer id);
	Auth findAuthByUserId(Integer userId);
	Auth findAuthByRank (String rank);
	
	Auth saveAuth(Auth auth);
	void deleteAuthById(Integer id);
	Auth updateAuth(Integer id, Auth newAuth);
}
