package nus.iss.team07.laps.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class Compensation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer compensationId;

	@ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "id")
	private Employee employee;
	
    @NotNull(message = "Start date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@NotNull(message = "Hours must be provided")
    @Min(value = 0, message = "Hours must be a positive number")
	private Integer Hours;
	
	@Column (length = 255)
	@Size(max = 255, message = "Reason have to be within 255 characters long")
	private String C_reason;
	
	@Column (length = 255)
	@Size(max = 255, message = "Comment have to be within 255 characters long")
	private String C_comment;
	
	//Deleted, Cancel, Approved, Reject, Applied/Updated
	private ApplicationStatus C_status;
	
	@ManyToOne 
	@JoinColumn(name="leaveType_id",referencedColumnName="id")
	private LeaveType leaveType;
	
	public Compensation() {}
	
	public Compensation(Employee employee_id, LocalDate startDate, Integer Hours, String C_reason, String C_comment, 
			ApplicationStatus C_status, LeaveType leaveType){
		this.employee = employee_id;
		this.startDate = startDate;
		this.Hours = Hours;
		this.C_reason = C_reason;
		this.C_comment = C_comment;
		this.C_status = C_status;
		this.leaveType = leaveType;
	}
	
	
    // Getter and Setter
    public Integer getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(Integer compensationId) {
        this.compensationId = compensationId;
    }
    
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
	
    
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	
	public Integer getHours() {
		return Hours;
	}
	public void setHours(Integer hours) {
		Hours = hours;
	}
	
	
	public ApplicationStatus getC_status() {
		return C_status;
	}
	public void setC_status(ApplicationStatus c_status) {
		C_status = c_status;
	}
	
	
	public String getC_reason() {
		return C_reason;
	}
	public void setC_reason(String c_reason) {
		C_reason = c_reason;
	}
	
	
	public String getC_comment() {
		return C_comment;
	}
	public void setC_comment(String c_comment) {
		C_comment = c_comment;
	}
	
	
	
	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	
	//Additional function
	
    public boolean isEditable() {
        return C_status == ApplicationStatus.Applied || C_status == ApplicationStatus.Updated;
    }

    public boolean isApprovable() {
        return C_status == ApplicationStatus.Approved;
    }
	
	@Override
	public String toString() { 	
		return "Compensation Details [EmpId=" + employee +", startDate="+ startDate + 
				", Hours=" + Hours + ", Reason=" + C_reason + ", Comments=" + C_comment
				+ ", Status=" + C_status + "]";
	}
	
}
