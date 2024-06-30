package nus.iss.team07.laps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.team07.laps.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	  @Query("Select e from Employee as e where e.name like CONCAT('%',:k,'%') ") 
	  List<Employee> SearchEmployeeByName(@Param("k") String keyword);
			 
	  @Query("Select e from Employee e where e.role.type like CONCAT('%',:k,'%') ") 
	  List<Employee> SearchEmployeeByJobRole(@Param("k") String keysword);
	
	  @Query("SELECT e FROM Employee e WHERE e.role.id =:role_id")
	  List<Employee> findByRoleId(@Param("role_id")Integer role_id);
	  
	  @Query("Select e from Employee e where e.department.id = :id")
	  List<Employee> findByDepartmentId(@Param("id")Integer id);
	
	  @Query("SELECT e FROM Employee e WHERE (LOWER(e.username)= LOWER(:uname) or LOWER(e.email) = Lower(:uname)) AND e.password=:pwd")
	  Employee findUserByNamePwd(@Param("uname")String username, @Param("pwd")String pwd);
	  
	  @Query("SELECT e FROM Employee e WHERE e.supervisor.id =:id ORDER BY e.name")
	  List<Employee> getEmployeeSupervisor(@Param("id")int id);
	 
	  @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.role.id = :id")
	  Boolean hasEmployeesWithThisRole(@Param("id") int roleId);
	 
	  @Query("SELECT DISTINCT e.supervisor FROM Employee e ")
	  List<Employee> findAllSupervisors();
	  
	  @Query("SELECT e FROM Employee e WHERE e.supervisor.id =:id AND e.name  LIKE %:keyword% ORDER BY e.name")
	  List<Employee> searchEmpUnderUs(@Param("id")int id, @Param("keyword") String keyword);

	  @Query("SELECT DISTINCT e FROM Employee e WHERE " +
	           "(e.department.id IN :departmentId)  AND " +
	           "(e.role.id IN :roleId)")
	  List<Employee> getSupervisorByDepartmentAndRole(@Param("departmentId") List<Integer> departmentId,
	                                                    @Param("roleId") List<Integer> roleId);	  
	  
}
