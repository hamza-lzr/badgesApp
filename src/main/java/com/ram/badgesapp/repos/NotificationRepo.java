package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByUser_Id(Long userId);
}
