package nus.iss.team07.laps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.team07.laps.model.Department;
import nus.iss.team07.laps.model.Employee;

public interface DepartmentRepository extends JpaRepository<Department, Integer>{
	@Query("Select d from Department as d where d.name like CONCAT('%',:k,'%') ") 
	public Department findByName(@Param("k") String name);
	
	@Query("Select DISTINCT e.supervisor from Employee e where e.department.id = :id ")
	public List<Employee> findSupervisors(@Param("id")Integer id);

	@Query("Select e from Employee e where e.department.id = :id ")
	public List<Employee> findEmployees(@Param("id")Integer id);
}
