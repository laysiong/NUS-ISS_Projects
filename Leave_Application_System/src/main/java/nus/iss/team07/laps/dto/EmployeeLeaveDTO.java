package nus.iss.team07.laps.dto;

import java.util.Map;


public class EmployeeLeaveDTO {
    private int employeeId;
    private String employeeName;
    private String department;
    private String role;
    private Map<String, Double> leaveBalances;
    
    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Map<String, Double> getLeaveBalances() {
        return leaveBalances;
    }

    public void setLeaveBalances(Map<String, Double> leaveBalances) {
        this.leaveBalances = leaveBalances;
    }
    
    
}
