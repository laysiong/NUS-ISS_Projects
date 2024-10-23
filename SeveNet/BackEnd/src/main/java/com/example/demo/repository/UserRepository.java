package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {

	//Count number of public users
	@Query("SELECT COUNT(u) FROM User u WHERE u.role.id = 1")
	Integer countUsers();
	
	@Query("Select u from User as u where u.name like CONCAT('%',:k,'%') OR u.username like CONCAT('%',:k,'%') ")
	List<User> findByName(@Param("k") String keyword);
	
	@Query("Select u from User as u where u.username like CONCAT('%',:k,'%') ")
	List<User> findAllByUserName(@Param("k") String keyword);

	@Query("Select u from User as u where u.username like CONCAT('%',:k,'%') ")
	Page<User> findAllByUserName(@Param("k") String keyword, Pageable pageable);
	
	@Query("SELECT u FROM User u JOIN u.followings f WHERE f.followingUser.username LIKE CONCAT('%', :k, '%')")
	List<User> findAllFollowingByUserName(@Param("k") String keyword);

	@Query("SELECT u FROM User u JOIN u.followings f WHERE f.followingUser.username LIKE CONCAT('%', :k, '%')")
	Page<User> findAllFollowingByUserName(@Param("k") String keyword, Pageable pageable);

	@Query("Select u from User u where u.role.type = :role ")
	List<User> findhByJobRole(@Param("role") String role);

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.role.id = :id")
	Boolean hasWithThisRole(@Param("id") int roleId);

	// ignore upper and lower case
	@Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
	List<User> findByUserNameIgnoreCase(@Param("username") String username);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUserName(@Param("username") String username);
}
