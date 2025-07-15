package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Notification;
import com.ram.badgesapp.repos.NotificationRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    public Notification getNotificationById(Long id) {
        return notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }

    public Notification createNotification(Notification notification) {
        return notificationRepo.save(notification);
    }

    public List<Notification> getNotificationsByUserId(Long id) {
        return notificationRepo.getNotificationsByUser_Id(id)
                ;
    }

    public void deleteNotification(Long id) {
        notificationRepo.deleteById(id);
    }

    public Notification updateNotificationRead(Long id){
        Notification n = notificationRepo.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));

        if(!n.isRead()) n.setRead(true);
        return notificationRepo.save(n);

    }


}
