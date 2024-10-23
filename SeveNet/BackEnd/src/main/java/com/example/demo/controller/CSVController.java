package com.example.demo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.interfacemethods.LabelInterface;
import com.example.demo.interfacemethods.ReportInterface;
import com.example.demo.interfacemethods.TagInterface;
import com.example.demo.interfacemethods.UserInterface;
import com.example.demo.model.Label;
import com.example.demo.model.Report;
import com.example.demo.model.Tag;
import com.example.demo.model.User;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/download")
public class CSVController {
	
	@Autowired
	private UserInterface userService;
	
	@Autowired
	private TagInterface tagService;
	
	@Autowired
	private ReportInterface reportService;
	
	
	@Autowired
	private LabelInterface labelService;
	
	@GetMapping("/user_csv")
	public void downloadUsers(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=users.csv");

		List<User> users = userService.findAllUsers();

		try (PrintWriter writer = response.getWriter()) {
			writer.write("ID,Name,Username,Status,Country,Gender,SocialScore,JoinedDate\n");
			for (User user : users) {
				writer.write(user.getId() + "," + user.getName() +","+ user.getUsername()+","+ 
			                 user.getStatus() +","+ user.getCountry() + "," + user.getGender()
			                 + ","+ user.getSocialScore() +"," + user.getJoinDate() + "\n");
			}
		}
	}
	
	
	@GetMapping("/pc_tags")
	public void downloadpc_tags(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=pc_tags.csv");

		List<Tag> Tags = tagService.findAllTag();

		try (PrintWriter writer = response.getWriter()) {
			writer.write("id,Remark,SpamTag, Tag,MsgId\n");
			for (Tag tag : Tags) {
				writer.write(tag.getId() + "," + tag.getRemark() +","+ tag.getTag()+","+ tag.getPCMsg().getId()+"\n");
			}
		}
	}
	
	@GetMapping("/reports")
	public void downloadreports(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=reports.csv");

		List<Report> Reports = reportService.findAllReports();

		try (PrintWriter writer = response.getWriter()) {
			writer.write("id,appealCount,caseCloseDate, reason,remarks,reportDate,reportedUserId,status,labelid,reporterId\n");
			for (Report report : Reports) {
				writer.write(report.getId() + "," + report.getAppealCount() +","+ report.getCaseCloseDate()+","+
			                 report.getReason()+","+  report.getRemarks()+","+ report.getReportDate() + "," + report.getReportedId() 
			                 +","+ report.getStatus()+"," +report.getLabel().getLabel()+","+ report.getReportUser().getId() +"\n");
				
			}
		}
	}
	
	@GetMapping("/labels")
	public void downloadlabels(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=labels.csv");

		List<Label> labels = labelService.findAllLabels();

		try (PrintWriter writer = response.getWriter()) {
			writer.write("id,color_code,label, penalty_score\n");
			for (Label label : labels) {
				writer.write(label.getId() + "," + label.getColorCode() +","+ label.getLabel()+","+
			                 label.getPenaltyScore()+"\n");
				
			}
		}
	}
	

}
