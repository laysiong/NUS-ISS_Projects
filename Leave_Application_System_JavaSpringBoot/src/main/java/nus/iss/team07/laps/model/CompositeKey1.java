package nus.iss.team07.laps.model;

import java.io.Serializable;
import java.util.Objects;

public class CompositeKey1 implements Serializable {
    private Integer employee;
    private int leaveType;

    // Default constructor
    public CompositeKey1() {}

    // Getters and setters
    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }

    public int getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(int leaveType) {
        this.leaveType = leaveType;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey1 that = (CompositeKey1) o;
        return leaveType == that.leaveType &&
               Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, leaveType);
    }
}
