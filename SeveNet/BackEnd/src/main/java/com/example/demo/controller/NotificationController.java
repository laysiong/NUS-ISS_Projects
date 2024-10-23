package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.dto.EmailRequest;
import com.example.demo.interfacemethods.NotificationInterface;
import com.example.demo.model.User;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationInterface notificationService;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendSimpleMessage(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email\n"+e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public List<Notification> getAllNotifications() {
        return notificationService.findAllNotifications();
    }

    @GetMapping("/findAllByUserId/{userId}")
    public List<Notification> getAllNotificationsByUserId(@PathVariable("userId") Integer userId) {
        return notificationService.findAllNotificationsByUserId(userId);
    }

    @GetMapping("/unReadList/{userId}")
    public List<Notification> getAllUnreadNotificationsByUserId(@PathVariable("userId") Integer userId) {
        return notificationService.findAllUnReadNotificationsByUserId(userId);
    }

    @GetMapping("/readList/{userId}")
    public List<Notification> getAllReadNotificationsByUserId(@PathVariable("userId") Integer userId) {
        return notificationService.findAllReadNotificationsByUserId(userId);
    }

    @GetMapping("/isRead/{id}")
    public Boolean isNotificationRead(@PathVariable("id") Integer id) {
        return notificationService.isNotificationReadById(id);
    }

    @PutMapping("/updateStatusToRead/{id}")
    public void markNotificationAsRead(@PathVariable("id") Integer id) {
        notificationService.updateNotificationStatusById(id);
    }

    @PostMapping("/create")
    public Notification saveNotification(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }

    @PostMapping("/sendToAllModerators")
    public void sendNotificationToAllModerators(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.getOrDefault("title", null);
        String message = requestBody.getOrDefault("message", null);
        notificationService.sendNotificationToAllModerators(title, message);
    }

    @PutMapping("/update/{id}")
    public Notification updateNotification(@PathVariable("id") Integer id, @RequestBody Notification newNotification) {
        return notificationService.updateNotificationById(id, newNotification);
    }

    @DeleteMapping("/delete{id}")
    public void deleteNotification(@PathVariable("id") Integer id) {
        notificationService.deleteNotificationById(id);
    }
}
