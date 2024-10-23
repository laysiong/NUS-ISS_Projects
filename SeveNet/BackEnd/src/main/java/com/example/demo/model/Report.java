package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.statusEnum.ReportStatus;
import jakarta.persistence.*;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status; // Complete, Pending, Appeal

    @Column(name = "report_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime reportDate;

    @Column(name = "case_close_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime caseCloseDate;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reportUser;

    // this is the id we are reporting (it could be pcmsg_id or user_id)
    // front-end logic: if report post/comment store 'p1' 'p2' as reportedId
    // if report user: store 'u1' 'u2' as reportedId
    private String reportedId;

    private Integer appealCount;

    // Constructors, getters and setters

    public Report() {
    }

    public Report( Label label, String reason, ReportStatus status, LocalDateTime reportDate, LocalDateTime caseCloseDate,String remarks, User user, String reportedId) {
        this.label = label;
        this.reason = reason;
        this.status = status;
        this.reportDate = reportDate;
        this.caseCloseDate = caseCloseDate;
        this.remarks = remarks;
        this.reportUser = user;
        this.reportedId = reportedId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getAppealCount() {
        return appealCount;
    }

    public void setAppealCount(Integer appealCount) {
        this.appealCount = appealCount;
    }

    public LocalDateTime getCaseCloseDate() {return caseCloseDate;}

    public void setCaseCloseDate(LocalDateTime caseCloseDate) {this.caseCloseDate = caseCloseDate;}

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public User getReportUser() {
        return reportUser;
    }

    public void setReportUser(User user) {
        this.reportUser = user;
    }

    public String getReportedId() {
        return reportedId;
    }

    public void setReportedId(String reportedId) {this.reportedId = reportedId;}
}