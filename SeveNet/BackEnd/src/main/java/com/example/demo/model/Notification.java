package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.statusEnum.NotificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Notification {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "Notification Id")
    private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "Banned User id")
	private User notificationUser;

	@Column (name = "Title", length = 255)
	private String title;

	@Column (name = "Message", length = 255)
	private String message;
	
	@Column (name = "Notification Time")
	private LocalDateTime notificationTime;
	
	@Enumerated(EnumType.STRING) // or EnumType.ORDINAL
	@Column (name = "Status")
	private NotificationStatus notificationStatus;
	
	
	public Notification () {}
	public Notification (User notificationUser, String title, String message, LocalDateTime notificationTime, NotificationStatus notificationStatus) {
		this.notificationUser = notificationUser;
		this.title = title;
		this.message = message;
		this.notificationTime = notificationTime;
		this.notificationStatus = notificationStatus;
	}
	
	//	Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getNotificationUser() {
		return notificationUser;
	}

	public void setNotificationUser(User notificationUser) {
		this.notificationUser = notificationUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getNotificationTime() {
		return notificationTime;
	}
	public void setNotificationTime(LocalDateTime notificationTime) {
		this.notificationTime = notificationTime;
	}
	public NotificationStatus getNotificationStatus() {
		return notificationStatus;
	}
	public void setNotificationStatus(NotificationStatus notificationStatus) {
		this.notificationStatus = notificationStatus;
	}
	
	
	
}
