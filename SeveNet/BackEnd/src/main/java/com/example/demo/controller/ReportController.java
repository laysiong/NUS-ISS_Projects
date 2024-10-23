package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.LabelDTO;
import com.example.demo.model.Label;
import com.example.demo.statusEnum.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.interfacemethods.ReportInterface;
import com.example.demo.model.Report;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportInterface reportService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.findAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/findByStatus/{status}")
    public ResponseEntity<List<Report>> getReportsByStatus(@PathVariable("status") ReportStatus status) {
        List<Report> reports = reportService.findReportsByStatus(status);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/findByUserId/{userId}")
    public ResponseEntity<List<Report>> getReportsByUserId(@PathVariable("userId") Integer userId) {
        List<Report> reports = reportService.findReportsByUserId(userId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/findByReportId/{reportId}")
    public ResponseEntity<LabelDTO> getLabelsByReportId(@PathVariable("reportId") Integer reportId) {
        LabelDTO label = reportService.findLabelByReportId(reportId);
        return ResponseEntity.ok(label);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable("id") Integer id) {
        Report report = reportService.findReportById(id);
        if (report != null) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report createdReport = reportService.saveReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable("id") Integer id, @RequestBody Report report) {
        Report updatedReport = reportService.updateReport(id, report);
        return ResponseEntity.ok(updatedReport);
    }

    @PutMapping("/addUpAppealCount/{id}")
    public ResponseEntity<Void> updateReport(@PathVariable("id") Integer id) {
        reportService.addUpAppealCount(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Report> updateReportStatus(@PathVariable("id") Integer id, @RequestParam("status") ReportStatus status) {
        Report updatedReport = reportService.updateReportStatusById(id, status);
        return ResponseEntity.ok(updatedReport);
    }

    @PutMapping("/closeCaseReport/{id}")
    public ResponseEntity<Report> closeCaseReport(@PathVariable("id") Integer id) {
        Report updatedReport = reportService.caseCloseReport(id);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable("id") Integer id) {
        reportService.deleteReportById(id);
        return ResponseEntity.noContent().build();
    }
}
