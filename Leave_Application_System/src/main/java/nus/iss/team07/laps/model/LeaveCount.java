package nus.iss.team07.laps.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@IdClass(CompositeKey1.class)
public class LeaveCount {
    @Id
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Id
    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    private double takenLeave;
    private double totalLeave;

    
    public LeaveCount() {}
    
    public LeaveCount(Employee employee, LeaveType leaveType, double totalLeave) {
        this.employee = employee;
        this.leaveType = leaveType;
        this.totalLeave = totalLeave;
    }
    
    
    // Getters and setters
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public double getTakenLeave() {
        return takenLeave;
    }

    public void setTakenLeave(double takenLeave) {
        this.takenLeave = takenLeave;
    }

    public double getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(double totalLeave) {
        this.totalLeave = totalLeave;
    }

    public double getAvailableLeave() {
        return totalLeave - takenLeave;
    }
    
    public double calculateCompensationClaim(int hours) {
        return Math.ceil(hours / 4) * 0.5;
    }

    public void addCompensationLeave(int hours) {
        double compensationLeave = calculateCompensationClaim(hours);
        setTotalLeave(this.totalLeave + compensationLeave);
    }
    


	public double checkRoleEntitlement(Role role, List<String> roles, double d, double e) {
		 if (roles.contains( role.getType())) {
	            return d;
	        } else {
	            return e;
	        }
	}

      
    
}
