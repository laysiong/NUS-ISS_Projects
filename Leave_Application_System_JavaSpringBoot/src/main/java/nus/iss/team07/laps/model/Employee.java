package nus.iss.team07.laps.model;


import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Employee {
	
    public interface LoginValidationGroup {}

	//Do we need to generate it like some code? STE20332E etc..
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column (length = 255)
	@Size(min=2, message="employee name must be at least 2 characters")
	private String name;
	
	@Column (length = 255)
	@Email(message = "Please provide a valid email")
	@NotBlank(message = "email is required")
	private String email;
		
	@ManyToOne
	@JoinColumn(name = "role_id")
    @JsonBackReference
	private Role role;
	
	@Column (length = 35)
	@NotBlank(message = "username is required")
	private String username;
	
	@Column (length = 35)
	private String password;
	
	@Column(name = "Contact_Number", length = 15)
	@NotBlank(message = "contact number is required")
	private String contactNum;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate join_date;
	
	@ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Employee supervisor;
	
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;
	
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<LeaveCount> leavecount;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<LeaveRecord> leaverecords;
	
	
	public Employee() {}
	
	public Employee(String name, String email, 
			Role role, String username, String password, Employee supervisor, 
			String contactnum, LocalDate joinDate, Department dept) 
	{
		this.name = name;
		this.email = email;
		this.role = role;
		this.username = username;
		this.password = password;
		this.contactNum = contactnum;
		this.join_date = joinDate;
		this.supervisor = supervisor;
		this.department = dept;

	}

	//getter and setter

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

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public LocalDate getJoin_date() {
		return join_date;
	}
	public void setJoin_date(LocalDate join_date) {
		this.join_date = join_date;
	}
	
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Employee getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Employee supervisor) {
		this.supervisor = supervisor;
	}

	public List<LeaveRecord> getLeaverecords() {
		return leaverecords;
	}

	public void setLeaverecords(List<LeaveRecord> leaverecords) {
		this.leaverecords = leaverecords;
	}

	
	public List<LeaveCount> getLeavercount() {
		return leavecount;
	}

	public void setLeavercount(List<LeaveCount> leavecount) {
		this.leavecount = leavecount;
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", email=" + email + ", supervisor=" + supervisor + ", department="
				+ department + ", leaverecords=" + leaverecords + "]";
	}	
	
	
	
}
