package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Notification;
import com.example.demo.statusEnum.NotificationStatus;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {

	@Query("SELECT n FROM Notification n WHERE n.notificationUser.id = :userId AND n.notificationStatus = :status")
	List<Notification> findByNotificationStatusByUserId(Integer userId, NotificationStatus status);
}
