package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.CongeRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BadgeLeaveDetectionService {

    private final BadgeRepo badgeRepo;
    private final CongeRepo congeRepo;
    private final UserEntityRepo userEntityRepo;
    private final NotificationService notificationService;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BadgeLeaveDetectionService(BadgeRepo badgeRepo, CongeRepo congeRepo, 
                                     UserEntityRepo userEntityRepo, 
                                     NotificationService notificationService) {
        this.badgeRepo = badgeRepo;
        this.congeRepo = congeRepo;
        this.userEntityRepo = userEntityRepo;
        this.notificationService = notificationService;
    }

    /**
     * Detects active badges for employees who are on leave
     * @return List of badges that are active during leave periods
     */
    public List<Badge> detectActiveBadgesDuringLeave() {
        List<Badge> activeBadgesDuringLeave = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        // Get all active leaves (congés) using the new repository method
        List<Conge> activeLeaves = congeRepo.findActiveLeaves(today);
        
        // For each active leave, check if the employee has active badges
        for (Conge leave : activeLeaves) {
            UserEntity user = leave.getUser();
            List<Badge> userBadges = badgeRepo.findAllByUser_Id(user.getId());
            
            // Filter active badges
            List<Badge> activeBadges = userBadges.stream()
                    .filter(badge -> badge.getStatus() == BadgeStatus.ACTIVE)
                    .toList();
            
            activeBadgesDuringLeave.addAll(activeBadges);
        }
        
        return activeBadgesDuringLeave;
    }
    
    /**
     * Finds all users with ADMIN role
     * @return List of admin users
     */
    public List<UserEntity> findAllAdminUsers() {
        return userEntityRepo.findAll().stream()
                .filter(user -> user.getRole() == Role.ADMIN)
                .collect(Collectors.toList());
    }
    
    /**
     * Notifies all admin users about active badges during leave periods
     * @param activeBadgesDuringLeave List of badges that are active during leave periods
     */
    public void notifyAdminsAboutActiveBadgesDuringLeave(List<Badge> activeBadgesDuringLeave) {
        if (activeBadgesDuringLeave.isEmpty()) {
            return; // No active badges during leave, no need to notify
        }
        
        List<UserEntity> adminUsers = findAllAdminUsers();
        LocalDate today = LocalDate.now();
        
        for (Badge badge : activeBadgesDuringLeave) {
            // Find the active leave period for this user using the repository method
            List<Conge> activeUserLeaves = congeRepo.findActiveLeavesByUserId(badge.getUser().getId(), today);
            
            if (!activeUserLeaves.isEmpty()) {
                Conge activeLeave = activeUserLeaves.get(0); // Get the first active leave
                
                String message = String.format(
                        "ALERT: Badge with code %s for employee %s %s (ID: %d) is active while the employee is on leave (from %s to %s)",
                        badge.getCode(),
                        badge.getUser().getFirstName(),
                        badge.getUser().getLastName(),
                        badge.getUser().getId(),
                        activeLeave.getStartDate(),
                        activeLeave.getEndDate()
                );
                
                // Notify all admin users
                for (UserEntity admin : adminUsers) {
                    notificationService.createNotificationForUser(admin.getId(), message);
                }
            }
        }
    }
    
    /**
     * Scheduled task to run the detection daily at 13:00
     */
    @Scheduled(cron = "0 0 13 * * *")
    public void scheduledBadgeLeaveDetection() {
        List<Badge> activeBadgesDuringLeave = detectActiveBadgesDuringLeave();
        notifyAdminsAboutActiveBadgesDuringLeave(activeBadgesDuringLeave);
    }

    @Transactional
    public void handleApprovedLeave(Conge leave) {
        LocalDate today = LocalDate.now();

        // Période de congé en cours (par rapport à "aujourd'hui") ?
        boolean leaveIsActiveNow = !today.isBefore(leave.getStartDate()) && !today.isAfter(leave.getEndDate());

        // Récupérer les badges de l'employé
        List<Badge> userBadges = badgeRepo.findAllByUser_Id(leave.getUser().getId());
        List<Badge> activeBadges = userBadges.stream()
                .filter(b -> b.getStatus() == BadgeStatus.ACTIVE)
                .toList();

        String range = leave.getStartDate().format(DATE_FMT) + " → " + leave.getEndDate().format(DATE_FMT);


        // Notifier les admins de l'approbation
        List<UserEntity> admins = findAllAdminUsers();
        String adminMsg = "Congé approuvé pour l'employé #" + leave.getUser().getId() + " (" + range + ").";
        for (UserEntity admin : admins) {
            notificationService.createNotificationForUser(admin.getId(), adminMsg);
        }

if (!leaveIsActiveNow) {
            // Congé approuvé mais à venir : simple info aux admins
            String info = "Le congé approuvé pour l'employé #" + leave.getUser().getId()
                    + " débutera le " + leave.getStartDate().format(DATE_FMT)
                    + ". Les badges seront vérifiés automatiquement à la date de début.";
            for (UserEntity admin : admins) {
                notificationService.createNotificationForUser(admin.getId(), info);
            }
        }
    }


}