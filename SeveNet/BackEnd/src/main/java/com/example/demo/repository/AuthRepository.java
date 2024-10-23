package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Auth;

public interface AuthRepository  extends JpaRepository<Auth,Integer>{
	
	@Query("SELECT COUNT(a) > 0 FROM Auth a WHERE a.rank = :rank AND a.menuViewJason = :menuViewJason")
	Boolean checkDuplicateRankAndMenuViewJason(String rank,String menuViewJason);
	
	@Query("SELECT a FROM Auth a JOIN a.users u WHERE u.id = :userId")
	Auth findByUserId(Integer userId);
	
	Auth findByRank(String rank);
}
