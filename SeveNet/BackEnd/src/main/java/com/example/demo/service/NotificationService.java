package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.configuration.WebSocketNotificationHandler;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.interfacemethods.NotificationInterface;
import com.example.demo.model.Notification;
import com.example.demo.statusEnum.NotificationStatus;
import com.example.demo.repository.NotificationRepository;

@Service      
@Transactional(readOnly = true)
public class NotificationService implements NotificationInterface {
	
	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private WebSocketNotificationHandler webSocketNotificationHandler;

    @Autowired
    private UserRepository userRepository;

	@Override
	public Notification findNotificationById(Integer id) {
		return notificationRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Notification not found with ID:" + id));
	}
	
	@Override
	public List<Notification> findAllNotifications() {
		return notificationRepository.findAll();
	}

	@Override
	public List<Notification> findAllNotificationsByUserId(Integer userId) {
		return notificationRepository.findAll()
				.stream()
				.filter(notif-> notif.getNotificationUser().getId().equals(userId))
				.collect(Collectors.toList());
	}

	@Override
	public List<Notification> findAllUnReadNotificationsByUserId(Integer userId) {
		return notificationRepository.findByNotificationStatusByUserId(userId, NotificationStatus.Unread);
	}

	@Override
	public List<Notification> findAllReadNotificationsByUserId(Integer userId) {
		return notificationRepository.findByNotificationStatusByUserId(userId, NotificationStatus.Read);
	}

	@Override
	public Boolean isNotificationReadById(Integer id) {
		Notification notification = findNotificationById(id);
        return notification.getNotificationStatus() == NotificationStatus.Read;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateNotificationStatusById(Integer id) {
		// can only change unread to read
		Notification notification = findNotificationById(id);
		Boolean isRead = notification.getNotificationStatus() == NotificationStatus.Read;
		if (!isRead) notification.setNotificationStatus(NotificationStatus.Read);
		notificationRepository.save(notification);
	}

	@Override
	@Transactional(readOnly = false)
	public Notification saveNotification(Notification notification) {
		notification.setNotificationStatus(NotificationStatus.Unread);
		notification.setNotificationTime(LocalDateTime.now());
		// activate WebSocket
		webSocketNotificationHandler.sendNotificationUpdate(notification.getNotificationUser().getId());
		return notificationRepository.save(notification);
	}

	@Override
	@Transactional(readOnly = false)
	public void sendNotificationToAllModerators(String title, String message) {
		// Default values
		if (title == null || title.isEmpty()) {
			title = "Reminder of user report";
		}
		if (message == null || message.isEmpty()) {
			message = "Our user has just reported, please take notice!";
		}

		LocalDateTime dateTime = LocalDateTime.now();
		List<User> moderators = userRepository.findhByJobRole("Moderator");
		for (User moderator : moderators) {
			Notification notification = new Notification(moderator, title, message, dateTime, NotificationStatus.Unread);
			notificationRepository.save(notification);
			webSocketNotificationHandler.sendNotificationUpdate(moderator.getId());
		}
	}

	@Override
	@Transactional(readOnly = false)
	public Notification updateNotificationById(Integer id, Notification newNotification) {
		return notificationRepository.findById(id).map(notification -> {
			notification.setNotificationUser(newNotification.getNotificationUser());
            notification.setMessage(newNotification.getMessage());
            notification.setNotificationTime(newNotification.getNotificationTime());
            notification.setNotificationStatus(newNotification.getNotificationStatus());
            return notificationRepository.save(notification);
        }).orElse(null);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteNotificationById(Integer id) {
		notificationRepository.deleteById(id);
	}
}
