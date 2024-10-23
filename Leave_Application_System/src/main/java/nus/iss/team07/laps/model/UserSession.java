package nus.iss.team07.laps.model;

import java.util.List;

import nus.iss.team07.laps.model.Employee;

public class UserSession {

	private Employee employee;
    private String roles;

    public UserSession() {}

    public UserSession(Employee employee, String roles) {
        this.employee = employee;
        this.roles = roles;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String role) {
        this.roles = role;
    }
}
