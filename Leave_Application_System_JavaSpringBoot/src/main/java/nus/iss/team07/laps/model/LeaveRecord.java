package nus.iss.team07.laps.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

@Entity
//@IdClass(CompositeKey2.class)
public class LeaveRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer Id;
	
	@ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "id")
	private Employee employee;
	
	@Column(length = 30)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@Column(length = 30)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	@Column(length = 100)
	private String reason;
	
	@Column(length = 50)
	private String remark;
	
	private String comment;
	
	@Column(length = 20)
	private ApplicationStatus status;
	
	
	@Column(length = 20)
	private String newContactNum;
	
	@ManyToOne 
	@JoinColumn(name="leaveType_id",referencedColumnName="id")
	private LeaveType leaveType;
	
    @Transient
    private LeaveType initialLeaveType;
	
    
    public LeaveType getInitialLeaveType() {
       return initialLeaveType;
    }
    
    public void setInitialLeaveType(LeaveType leaveType) {
       this.initialLeaveType = leaveType;
    }
   
	private long numOfoff;
	
	
	public LeaveRecord() {}
	
	//For testing if caught in Bean
	public LeaveRecord(LocalDate stateDate, LocalDate endDate, String leaveReason) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = leaveReason;
	}
	
	//To create data
	public LeaveRecord(Employee employee_id,LocalDate startDate,LocalDate endDate,String leaveReason, 
			 String remark,String comment,ApplicationStatus status,String newContactNum,LeaveType leaveType, long numOfoff) {
		this.employee = employee_id;
		this.startDate=startDate;
		this.endDate=endDate;
		this.reason=leaveReason;
		this.remark=remark;
		this.comment=comment;
		this.status=status;
		this.newContactNum=newContactNum;
		this.leaveType=leaveType;
		this.numOfoff = numOfoff;
	}
	
	//For forms (employee id will get from session)
	public LeaveRecord(LocalDate startDate,LocalDate endDate,String leaveReason, 
			 String remark,String comment,ApplicationStatus status,String newContactNum,LeaveType leaveType) {
		this.startDate=startDate;
		this.endDate=endDate;
		this.reason=leaveReason;
		this.remark=remark;
		this.comment=comment;
		this.newContactNum=newContactNum;
		this.leaveType=leaveType;
	}
	
	
	
	public Integer getId() {
		return Id;
	}

	public void setId(Integer Id) {
		this.Id = Id;
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

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus applied) {
		this.status = applied;
	}
	
	public String getNewContactNum() {
		return newContactNum;
	}
	public void setNewContactNum(String newContactNum) {
		this.newContactNum = newContactNum;
	}
	
	
	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	
    
	public long getNumOfoff() {
		return numOfoff;
	}

	public void setNumOfoff(long numOfoff) {
		this.numOfoff = numOfoff;
	}
	
	//Additional function
	
    public boolean isEditable() {
        return status == ApplicationStatus.Applied || status == ApplicationStatus.Updated;
    }

    public boolean isApprovable() {
        return status == ApplicationStatus.Approved;
    }
	
    
	public long datediff() {
		return ChronoUnit.DAYS.between(this.startDate, this.endDate);
	}


	@Override
	public String toString() {
		return "LeaveRecord [employee=" + employee + ", startDate=" + startDate + ", endDate=" + endDate + ", status="
				+ status + ", leaveType=" + leaveType + "]";
	}
	
	
}
