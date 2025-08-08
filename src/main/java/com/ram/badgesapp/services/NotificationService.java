package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.Notification;
import com.ram.badgesapp.entities.ReqStatus;
import com.ram.badgesapp.entities.Requests;
import com.ram.badgesapp.repos.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final BadgeRepo badgeRepo;
    private final AccessRepo accessRepo;
    private final UserEntityRepo userEntityRepo;
    private final RequestsRepo requestsRepo;

    public NotificationService(NotificationRepo notificationRepo, BadgeRepo badgeRepo, AccessRepo accessRepo, UserEntityRepo userEntityRepo, RequestsRepo requestsRepo) {
        this.notificationRepo = notificationRepo;
        this.badgeRepo = badgeRepo;
        this.accessRepo = accessRepo;
        this.userEntityRepo = userEntityRepo;
        this.requestsRepo = requestsRepo;
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

    @Scheduled(cron = "0 0 8 * * ?")
    public void checkExpiringBadgesAndAccesses() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekFromNow = today.plusDays(7);

        // ✅ Badges expiring soon
        List<Badge> expiringBadges = badgeRepo.findByExpiryDateBetween(today, oneWeekFromNow);
        for (Badge badge : expiringBadges) {
            String message = String.format(
                    "Your badge with the code %s is expiring on %s. Please renew it.",
                    badge.getCode(),
                    badge.getExpiryDate()
            );

            createNotificationForUser(badge.getUser().getId(), message);
        }

        // ✅ Accesses expiring soon
        List<Access> expiringAccesses = accessRepo.findByEndDateBetween(today, oneWeekFromNow);
        for (Access access : expiringAccesses) {
            String message = String.format(
                    "Your access #%d for your badge %s linked to the airport %s is expiring on %s. Please renew it.",
                    access.getId(),
                    access.getBadge().getCode(),
                    access.getAirport().getName(),
                    access.getEndDate()
            );

            createNotificationForUser(access.getBadge().getUser().getId(), message);
        }
    }

    public void createNotificationForUser(Long userId, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(userEntityRepo.findById(userId).get()); // if you have a direct `User` relation, set the `user` object instead
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        notificationRepo.save(notification);
    }

    public void markAllAsReadForUser(Long userId) {
        List<Notification> notifications = notificationRepo.getNotificationsByUser_Id(userId);
        for (Notification n : notifications) {
            if (!n.isRead()) {
                n.setRead(true);
            }
        }
        notificationRepo.saveAll(notifications);
    }
    
    /**
     * Sends a notification to a user when their request status changes from pending to approved or rejected
     * @param requestId The ID of the request that has changed status
     * @param oldStatus The previous status of the request
     * @param newStatus The new status of the request
     */
    public void notifyAboutRequestStatus(Long requestId, ReqStatus oldStatus, ReqStatus newStatus) {
        // Only send notification if status changed from PENDING to APPROVED or REJECTED
        if (oldStatus == ReqStatus.PENDING && (newStatus == ReqStatus.APPROVED || newStatus == ReqStatus.REJECTED)) {
            // Get the request to access user information
            Requests request = requestsRepo.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("Request not found with id: " + requestId));
            
            // Create appropriate message based on new status
            String statusText = newStatus == ReqStatus.APPROVED ? "approved" : "rejected";
            String message = String.format(
                    "Your request has been %s. Request type: %s, Description: %s",
                    statusText,
                    request.getReqType(),
                    request.getDescription()
            );
            
            // Send notification to the user
            createNotificationForUser(request.getUser().getId(), message);
        }
    }



}
