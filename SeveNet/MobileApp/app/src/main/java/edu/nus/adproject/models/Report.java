package edu.nus.adproject.models;

import java.time.LocalDateTime;

public class Report {
    private int id;
    private TypeOfReport typeOfReport;
    private String reason;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String remarks;
    private User user;

    public Report() {}

    public Report(TypeOfReport typeOfReport, String reason, String status, LocalDateTime startDate,
                  String remarks, LocalDateTime endDate, User user) {
        this.typeOfReport = typeOfReport;
        this.reason = reason;
        this.status =status;
        this.startDate = startDate;
        this.remarks = remarks;
        this.endDate = endDate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeOfReport getTypeOfReport() {
        return typeOfReport;
    }

    public void setTypeOfReport(TypeOfReport typeOfReport) {
        this.typeOfReport = typeOfReport;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
