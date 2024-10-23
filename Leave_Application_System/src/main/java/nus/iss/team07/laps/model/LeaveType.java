package nus.iss.team07.laps.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class LeaveType 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 30)
	private String type;
	
	private double default_val;
	// orphanRemoval = true
	@OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LeaveCount> leaveCount;

	@OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Compensation> compensations;
	 
    @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaveRecord> leaveRecords;
    
	public LeaveType() {}
	
	public LeaveType(String Type, double default_val) {
		this.type = Type;
		this.default_val = default_val;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getDefault_val() {
		return default_val;
	}

	public void setDefault_val(double default_val) {
		this.default_val = default_val;
	}
	

}
