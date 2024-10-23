package nus.iss.team07.laps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.team07.laps.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	@Query("Select r from Role as r where r.type = :k ") 
	Role findByType(@Param("k") String keyword);
}
