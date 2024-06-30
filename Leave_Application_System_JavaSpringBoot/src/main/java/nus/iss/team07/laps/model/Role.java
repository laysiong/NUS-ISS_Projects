package nus.iss.team07.laps.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 30)
	private String type;
	
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonManagedReference
	private List<Employee> employees;
	
	public Role() {
	}
	
	public Role(String type) {
		this.type = type;
	}
	
	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}
}
