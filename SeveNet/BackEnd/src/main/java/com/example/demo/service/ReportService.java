package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.configuration.WebSocketReportHandler;
import com.example.demo.dto.LabelDTO;
import com.example.demo.exception.DuplicateReportException;
import com.example.demo.model.Label;
import com.example.demo.model.User;
import com.example.demo.statusEnum.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.interfacemethods.ReportInterface;
import com.example.demo.model.Report;
import com.example.demo.repository.ReportRepository;

@Service
@Transactional(readOnly = true)
public class ReportService implements ReportInterface {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketReportHandler webSocketReportHandler;

    @Override
	public Integer CountReports() {
		return reportRepository.countReport();
	}
    
    @Override
	public List<Report> findTop5ByOrderByDateDesc() {
		return reportRepository.findTop5ByOrderByReportDateDesc();
	}
      
    @Override
    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public List<Report> findReportsByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

    @Override
    public List<Report> findReportsByUserId(Integer userId) {
        return reportRepository.findByReportUserId(userId);
    }

    @Override
    public LabelDTO findLabelByReportId(Integer id) {
        Label label = reportRepository.findLabelByReportId(id);
        return new LabelDTO(label);
    }

    @Override
    public Report findReportById(Integer id) {
        return reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = false)
    public Report saveReport(Report report) {
        handleDuplicateReportException(report);
        webSocketReportHandler.sendReportUpdate(report.getReportUser().getId());
        return reportRepository.save(report);
    }

    @Override
    @Transactional(readOnly = false)
    public Report updateReport(Integer id, Report report) {
        Report existingReport = findReportById(id);
        existingReport.setLabel(report.getLabel());
        existingReport.setReason(report.getReason());
        existingReport.setStatus(report.getStatus());
        existingReport.setReportDate(report.getReportDate());
        existingReport.setCaseCloseDate(report.getCaseCloseDate());
        existingReport.setRemarks(report.getRemarks());
        existingReport.setAppealCount(report.getAppealCount());
        existingReport.setReportUser(report.getReportUser());
        existingReport.setReportedId(report.getReportedId());
        webSocketReportHandler.sendReportUpdate(report.getReportUser().getId());
        return reportRepository.save(existingReport);
    }

    @Override
    public Report updateReportStatusById(Integer id, ReportStatus status) {
        Report currentReport = findReportById(id);
        currentReport.setStatus(status);
        return reportRepository.save(currentReport);
    }

    @Override
    public Report caseCloseReport(Integer id) {
        Report report = findReportById(id);
        report.setStatus(ReportStatus.Complete);
        report.setCaseCloseDate(LocalDateTime.now());
        return reportRepository.save(report);
    }

    @Override
    @Transactional(readOnly = false)
    public void addUpAppealCount(Integer id) {
        Report report = findReportById(id);
        report.setAppealCount(report.getAppealCount() + 1);
        reportRepository.save(report);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteReportById(Integer id) {
        reportRepository.deleteById(id);
    }

    private void handleDuplicateReportException(Report report) {
        if (reportRepository.checkDuplicateReport(report.getReportUser().getId(), report.getReportedId(), report.getLabel().getId())) {
            throw new DuplicateReportException("Your report is still pending. please wait patiently");
        }
    }

}