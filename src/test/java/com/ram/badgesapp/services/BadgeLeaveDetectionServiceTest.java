package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.CongeRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private UserEntity employee;
    private UserEntity admin;
    private Badge activeBadge;
    private Conge activeLeave;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        employee = new UserEntity();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setRole(Role.EMPLOYEE);

        admin = new UserEntity();
        admin.setId(2L);
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@example.com");
        admin.setRole(Role.ADMIN);

        activeBadge = new Badge();
        activeBadge.setId(1L);
        activeBadge.setCode("B12345");
        activeBadge.setStatus(BadgeStatus.ACTIVE);
        activeBadge.setUser(employee);

        LocalDate today = LocalDate.now();
        activeLeave = new Conge();
        activeLeave.setId(1L);
        activeLeave.setStartDate(today.minusDays(1));
        activeLeave.setEndDate(today.plusDays(5));
        activeLeave.setUser(employee);
        activeLeave.setStatus(StatusConge.APPROVED);
    }

    @Test
    void detectActiveBadgesDuringLeave_shouldReturnActiveBadges() {
        // Arrange
        LocalDate today = LocalDate.now();
        when(congeRepo.findActiveLeaves(today)).thenReturn(Collections.singletonList(activeLeave));
        when(badgeRepo.findAllByUser_Id(employee.getId())).thenReturn(Collections.singletonList(activeBadge));

        // Act
        List<Badge> result = badgeLeaveDetectionService.detectActiveBadgesDuringLeave();

        // Assert
        assertEquals(1, result.size());
        assertEquals(activeBadge.getId(), result.get(0).getId());
        verify(congeRepo).findActiveLeaves(today);
        verify(badgeRepo).findAllByUser_Id(employee.getId());
    }

    @Test
    void findAllAdminUsers_shouldReturnAdminUsers() {
        // Arrange
        when(userEntityRepo.findAll()).thenReturn(Arrays.asList(employee, admin));

        // Act
        List<UserEntity> result = badgeLeaveDetectionService.findAllAdminUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(admin.getId(), result.get(0).getId());
        verify(userEntityRepo).findAll();
    }

    @Test
    void notifyAdminsAboutActiveBadgesDuringLeave_shouldNotifyAdmins() {
        // Arrange
        LocalDate today = LocalDate.now();
        when(congeRepo.findActiveLeavesByUserId(employee.getId(), today)).thenReturn(Collections.singletonList(activeLeave));
        when(userEntityRepo.findAll()).thenReturn(Collections.singletonList(admin));

        // Act
        badgeLeaveDetectionService.notifyAdminsAboutActiveBadgesDuringLeave(Collections.singletonList(activeBadge));

        // Assert
        verify(notificationService).createNotificationForUser(eq(admin.getId()), anyString());
    }

    @Test
    void notifyAdminsAboutActiveBadgesDuringLeave_shouldNotNotifyWhenNoActiveBadges() {
        // Act
        badgeLeaveDetectionService.notifyAdminsAboutActiveBadgesDuringLeave(Collections.emptyList());

        // Assert
        verifyNoInteractions(userEntityRepo);
        verifyNoInteractions(notificationService);
    }

    @Test
    void scheduledBadgeLeaveDetection_shouldDetectAndNotify() {
        // Arrange
        LocalDate today = LocalDate.now();
        when(congeRepo.findActiveLeaves(today)).thenReturn(Collections.singletonList(activeLeave));
        when(badgeRepo.findAllByUser_Id(employee.getId())).thenReturn(Collections.singletonList(activeBadge));
        when(congeRepo.findActiveLeavesByUserId(employee.getId(), today)).thenReturn(Collections.singletonList(activeLeave));
        when(userEntityRepo.findAll()).thenReturn(Collections.singletonList(admin));

        // Act
        badgeLeaveDetectionService.scheduledBadgeLeaveDetection();

        // Assert
        verify(notificationService).createNotificationForUser(eq(admin.getId()), anyString());
    }
}