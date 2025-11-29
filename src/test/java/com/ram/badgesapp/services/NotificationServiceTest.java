package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.AccessRepo;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.NotificationRepo;
import com.ram.badgesapp.repos.RequestsRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private BadgeRepo badgeRepo;

    @Mock
    private AccessRepo accessRepo;

    @Mock
    private UserEntityRepo userEntityRepo;

    @Mock
    private RequestsRepo requestsRepo;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private UserEntity user;
    private Badge badge;
    private Access access;
    private Requests request;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        notification.setRead(false);

        badge = new Badge();
        badge.setId(1L);
        badge.setUser(user);
        badge.setCode("B001");
        badge.setExpiryDate(LocalDate.now().plusDays(3));

        Airport airport = new Airport();
        airport.setId(1L);
        airport.setName("Test Airport");

        access = new Access();
        access.setId(1L);
        access.setBadge(badge);
        access.setAirport(airport);
        access.setEndDate(LocalDate.now().plusDays(4));
        
        request = new Requests();
        request.setId(1L);
        request.setUser(user);
        request.setReqType(ReqType.NEW_BADGE);
        request.setDescription("Test Request");
    }

    @Test
    void getAllNotifications() {
        when(notificationRepo.findAll()).thenReturn(Collections.singletonList(notification));
        List<Notification> notifications = notificationService.getAllNotifications();
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
    }

    @Test
    void getNotificationById() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        Notification foundNotification = notificationService.getNotificationById(1L);
        assertNotNull(foundNotification);
        assertEquals(1L, foundNotification.getId());
    }

    @Test
    void createNotification() {
        when(notificationRepo.save(any(Notification.class))).thenReturn(notification);
        Notification savedNotification = notificationService.createNotification(new Notification());
        assertNotNull(savedNotification);
    }

    @Test
    void getNotificationsByUserId() {
        when(notificationRepo.getNotificationsByUser_Id(1L)).thenReturn(Collections.singletonList(notification));
        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
    }

    @Test
    void deleteNotification() {
        doNothing().when(notificationRepo).deleteById(1L);
        notificationService.deleteNotification(1L);
        verify(notificationRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateNotificationRead() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepo.save(any(Notification.class))).thenReturn(notification);
        Notification updatedNotification = notificationService.updateNotificationRead(1L);
        assertTrue(updatedNotification.isRead());
    }
    
    @Test
    void updateNotificationRead_notFound() {
        when(notificationRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> notificationService.updateNotificationRead(1L));
    }


    @Test
    void checkExpiringBadgesAndAccesses() {
        when(badgeRepo.findByExpiryDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(badge));
        when(accessRepo.findByEndDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(access));
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));

        notificationService.checkExpiringBadgesAndAccesses();

        verify(notificationRepo, times(2)).save(any(Notification.class));
    }

    @Test
    void createNotificationForUser() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));
        notificationService.createNotificationForUser(1L, "Test message");
        verify(notificationRepo, times(1)).save(any(Notification.class));
    }

    @Test
    void markAllAsReadForUser() {
        when(notificationRepo.getNotificationsByUser_Id(1L)).thenReturn(Collections.singletonList(notification));
        notificationService.markAllAsReadForUser(1L);
        verify(notificationRepo, times(1)).saveAll(anyList());
        assertTrue(notification.isRead());
    }
    
    @Test
    void notifyAboutRequestStatus_pendingToApproved() {
        when(requestsRepo.findById(1L)).thenReturn(Optional.of(request));
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));

        notificationService.notifyAboutRequestStatus(1L, ReqStatus.PENDING, ReqStatus.APPROVED);

        verify(notificationRepo, times(1)).save(any(Notification.class));
    }
    
    @Test
    void notifyAboutRequestStatus_otherTransitions() {
        notificationService.notifyAboutRequestStatus(1L, ReqStatus.APPROVED, ReqStatus.REJECTED);
        verify(notificationRepo, never()).save(any(Notification.class));
    }
}
