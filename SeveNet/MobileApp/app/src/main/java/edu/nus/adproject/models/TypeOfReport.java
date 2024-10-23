package edu.nus.adproject.models;

import java.util.List;

public class TypeOfReport {
    private int id;
    private String reportType;
    private int weight;
    private List<Report> reports;

    public TypeOfReport() {}

    public TypeOfReport(String reportType, int weight) {
        this.reportType = reportType;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
