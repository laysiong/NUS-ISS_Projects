package nus.iss.team07.laps.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "d_name", length = 30)
	private String name;
	
	@Column(length = 15)
	private String contact;
	
	@Column(length = 15)
	private String floor;
	
	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
	private List<Employee> employees;
	
	public Department() {
		
	}
	
	public Department(String name, String contact, String floor) {
		this.name = name;
		this.contact = contact;
		this.floor = floor;
	}


	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}
	
}
