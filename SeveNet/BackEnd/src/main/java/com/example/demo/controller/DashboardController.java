package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.PCMsg;
import com.example.demo.model.Report;
import com.example.demo.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
	@Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Integer>> getDashboardCounts() {
        Map<String, Integer> counts = dashboardService.getDashboardCounts();
        return ResponseEntity.ok(counts);
    }
    
    @GetMapping("/getLatest5Report")
    public ResponseEntity<List<Report>> getLatestReports() {
    	return ResponseEntity.ok(dashboardService.getLatestReports());
    }
    
    @GetMapping("/Top5Posts")
    public ResponseEntity<List<PCMsg>> getTop5Posts() {
    	return ResponseEntity.ok(dashboardService.getTop5Posts());
    }
    
    @GetMapping("/Top5HotPosts")
    public ResponseEntity<List<PCMsg>> getTop5HotPosts() {
    	return ResponseEntity.ok(dashboardService.getTop5HotPosts());
    }
    
  
}
