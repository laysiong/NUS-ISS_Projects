package nus.iss.team07.laps.dto;

import java.util.List;

public class LeaveEntitlementForm {
	
	    private Integer employeeId;
	    private List<LeaveCountDTO> leaveCounts;

	    // Getters and Setters
	    public Integer getEmployeeId() {
	        return employeeId;
	    }

	    public void setEmployeeId(Integer employeeId) {
	        this.employeeId = employeeId;
	    }

	    public List<LeaveCountDTO> getLeaveCounts() {
	        return leaveCounts;
	    }

	    public void setLeaveCounts(List<LeaveCountDTO> leaveCounts) {
	        this.leaveCounts = leaveCounts;
	    }

	    // Nested LeaveCountDTO Class
	    public static class LeaveCountDTO {
	        private Integer leaveTypeId;
	        private String leaveType;
	        private Double totalLeave;
	        private Double takenLeave;

	        // Getters and Setters
	        public Integer getLeaveTypeId() {
	            return leaveTypeId;
	        }

	        public void setLeaveTypeId(Integer leaveTypeId) {
	            this.leaveTypeId = leaveTypeId;
	        }

	        
	        public String getLeaveType() {
	            return leaveType;
	        }

	        public void setLeaveType(String leaveType) {
	            this.leaveType = leaveType;
	        }

	        public Double getTotalLeave() {
	            return totalLeave;
	        }

	        public void setTotalLeave(Double totalLeave) {
	            this.totalLeave = totalLeave;
	        }

	        public Double getTakenLeave() {
	            return takenLeave;
	        }

	        public void setTakenLeave(Double takenLeave) {
	            this.takenLeave = takenLeave;
	        }
	    }

}