package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.CongeRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BadgeLeaveDetectionServiceTest {

    @Mock
    private BadgeRepo badgeRepo;

    @Mock
    private CongeRepo congeRepo;

    @Mock
    private UserEntityRepo userEntityRepo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BadgeLeaveDetectionService badgeLeaveDetectionService;

    private UserEntity user;
    private UserEntity admin;
    private Badge badge;
    private Conge conge;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.EMPLOYEE);

        admin = new UserEntity();
        admin.setId(2L);
        admin.setRole(Role.ADMIN);

        badge = new Badge();
        badge.setId(1L);
        badge.setUser(user);
        badge.setStatus(BadgeStatus.ACTIVE);
        badge.setCode("BADGE001");

        conge = new Conge();
        conge.setId(1L);
        conge.setUser(user);
        conge.setStartDate(LocalDate.now().minusDays(1));
        conge.setEndDate(LocalDate.now().plusDays(1));
    }

    @Test
    void detectActiveBadgesDuringLeave() {
        when(congeRepo.findActiveLeaves(any(LocalDate.class))).thenReturn(Collections.singletonList(conge));
        when(badgeRepo.findAllByUser_Id(1L)).thenReturn(Collections.singletonList(badge));

        List<Badge> activeBadges = badgeLeaveDetectionService.detectActiveBadgesDuringLeave();

        assertEquals(1, activeBadges.size());
        assertEquals(badge, activeBadges.get(0));
    }

    @Test
    void findAllAdminUsers() {
        when(userEntityRepo.findAll()).thenReturn(Collections.singletonList(admin));
        List<UserEntity> admins = badgeLeaveDetectionService.findAllAdminUsers();
        assertEquals(1, admins.size());
        assertEquals(admin, admins.get(0));
    }

    @Test
    void notifyAdminsAboutActiveBadgesDuringLeave() {
        when(userEntityRepo.findAll()).thenReturn(Collections.singletonList(admin));
        when(congeRepo.findActiveLeavesByUserId(anyLong(), any(LocalDate.class))).thenReturn(Collections.singletonList(conge));

        badgeLeaveDetectionService.notifyAdminsAboutActiveBadgesDuringLeave(Collections.singletonList(badge));

        verify(notificationService, times(1)).createNotificationForUser(eq(2L), anyString());
    }
    
    @Test
    void notifyAdminsAboutActiveBadgesDuringLeave_noBadges() {
        badgeLeaveDetectionService.notifyAdminsAboutActiveBadgesDuringLeave(Collections.emptyList());
        verify(notificationService, never()).createNotificationForUser(anyLong(), anyString());
    }
    
    @Test
    void handleApprovedLeave() {
        when(userEntityRepo.findAll()).thenReturn(Collections.singletonList(admin));
        when(badgeRepo.findAllByUser_Id(1L)).thenReturn(Collections.singletonList(badge));

        badgeLeaveDetectionService.handleApprovedLeave(conge);

        verify(notificationService, times(1)).createNotificationForUser(eq(2L), anyString());
    }

    @Test
    void handleApprovedLeave_futureLeave() {
        conge.setStartDate(LocalDate.now().plusDays(2));
        conge.setEndDate(LocalDate.now().plusDays(5));
        when(userEntityRepo.findAll()).thenReturn(List.of(admin));
        
        badgeLeaveDetectionService.handleApprovedLeave(conge);

        // Notify admins about approval
        verify(notificationService, times(1)).createNotificationForUser(
            eq(admin.getId()), 
            contains("Congé approuvé pour l'employé #1")
        );
        
        // Notify admins about future check
        verify(notificationService, times(1)).createNotificationForUser(
            eq(admin.getId()), 
            contains("Le congé approuvé pour l'employé #1 débutera le")
        );
    }
}
