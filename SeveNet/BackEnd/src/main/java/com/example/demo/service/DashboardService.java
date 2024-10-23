package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.PCMsg;
import com.example.demo.model.Report;

@Service
public class DashboardService {
	
	@Autowired
    private ReportService reportService;
    
    @Autowired
    private PCMsgService PCMsgService;
    
    @Autowired
    private UserService userService;
    
    public Map<String, Integer> getDashboardCounts() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("reports", reportService.CountReports());
        counts.put("posts", PCMsgService.CountPosts());
        counts.put("comments", PCMsgService.CountComments());
        counts.put("users", userService.CountUsers());
        return counts;
    }
    
    public List<Report> getLatestReports() {
        return reportService.findTop5ByOrderByDateDesc();
    }
    
    public List<PCMsg> getTop5Posts() {
        return PCMsgService.findTop5Posts();
    }
   
    public List<PCMsg> getTop5HotPosts() {
        return PCMsgService.findTop5HotPosts();
    }
}
