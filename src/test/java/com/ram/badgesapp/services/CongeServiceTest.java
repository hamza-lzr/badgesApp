package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Conge;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.StatusConge;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.CongeRepo;
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
class CongeServiceTest {

    @Mock
    private CongeRepo congeRepo;

    @Mock
    private NotificationService notificationService;

    @Mock
    private BadgeLeaveDetectionService badgeLeaveDetectionService;

    @Mock
    private UserEntityRepo userEntityRepo;

    @InjectMocks
    private CongeService congeService;

    private Conge conge;
    private UserEntity user;
    private UserEntity admin;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");

        admin = new UserEntity();
        admin.setId(2L);
        admin.setRole(Role.ADMIN);

        conge = new Conge();
        conge.setId(1L);
        conge.setUser(user);
        conge.setStartDate(LocalDate.now());
        conge.setEndDate(LocalDate.now().plusDays(5));
        conge.setStatus(StatusConge.PENDING);
        conge.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getCongeById() {
        when(congeRepo.findById(1L)).thenReturn(Optional.of(conge));
        Optional<Conge> foundConge = congeService.getCongeById(1L);
        assertTrue(foundConge.isPresent());
        assertEquals(1L, foundConge.get().getId());
    }

    @Test
    void saveConge() {
        when(congeRepo.save(any(Conge.class))).thenReturn(conge);
        when(userEntityRepo.findAllByRole(Role.ADMIN)).thenReturn(Collections.singletonList(admin));

        Conge newConge = new Conge();
        newConge.setUser(user);
        newConge.setStartDate(LocalDate.now());
        newConge.setEndDate(LocalDate.now().plusDays(5));

        Conge savedConge = congeService.saveConge(newConge);

        assertNotNull(savedConge);
        assertEquals(StatusConge.PENDING, savedConge.getStatus());
        verify(notificationService, times(1)).createNotificationForUser(eq(1L), anyString());
        verify(notificationService, times(1)).createNotificationForUser(eq(2L), anyString());
    }

    @Test
    void deleteConge() {
        doNothing().when(congeRepo).deleteById(1L);
        congeService.deleteConge(1L);
        verify(congeRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateConge() {
        when(congeRepo.findById(1L)).thenReturn(Optional.of(conge));
        when(congeRepo.save(any(Conge.class))).thenReturn(conge);

        Conge patch = new Conge();
        patch.setDescription("Updated description");

        Optional<Conge> updatedConge = congeService.updateConge(1L, patch);

        assertTrue(updatedConge.isPresent());
        assertEquals("Updated description", updatedConge.get().getDescription());
    }
    
    @Test
    void updateConge_toApproved() {
        when(congeRepo.findById(1L)).thenReturn(Optional.of(conge));
        
        Conge savedConge = new Conge();
        savedConge.setId(1L);
        savedConge.setUser(user);
        savedConge.setStatus(StatusConge.APPROVED);
        savedConge.setStartDate(conge.getStartDate());
        savedConge.setEndDate(conge.getEndDate());

        when(congeRepo.save(any(Conge.class))).thenReturn(savedConge);

        Conge patch = new Conge();
        patch.setStatus(StatusConge.APPROVED);

        congeService.updateConge(1L, patch);

        verify(notificationService, times(1)).createNotificationForUser(eq(1L), anyString());
        verify(badgeLeaveDetectionService, times(1)).handleApprovedLeave(any(Conge.class));
    }


    @Test
    void updateStatusToApproved() {
        when(congeRepo.findById(1L)).thenReturn(Optional.of(conge));
        when(congeRepo.save(any(Conge.class))).thenReturn(conge);

        Optional<Conge> updatedConge = congeService.updateStatusToApproved(1L);

        assertTrue(updatedConge.isPresent());
        assertEquals(StatusConge.APPROVED, updatedConge.get().getStatus());
        verify(notificationService, times(1)).createNotificationForUser(eq(1L), anyString());
        verify(badgeLeaveDetectionService, times(1)).handleApprovedLeave(any(Conge.class));
    }
    
    @Test
    void updateStatusToApproved_notFound() {
        when(congeRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            congeService.updateStatusToApproved(1L);
        });
    }

    @Test
    void updateStatusToRejected() {
        when(congeRepo.findById(1L)).thenReturn(Optional.of(conge));
        when(congeRepo.save(any(Conge.class))).thenReturn(conge);

        Optional<Conge> updatedConge = congeService.updateStatusToRejected(1L);

        assertTrue(updatedConge.isPresent());
        assertEquals(StatusConge.REJECTED, updatedConge.get().getStatus());
        verify(notificationService, times(1)).createNotificationForUser(eq(1L), anyString());
    }

    @Test
    void getAllConges() {
        when(congeRepo.findAll()).thenReturn(Collections.singletonList(conge));
        List<Conge> conges = congeService.getAllConges();
        assertFalse(conges.isEmpty());
        assertEquals(1, conges.size());
    }

    @Test
    void getCongesByUserId() {
        when(congeRepo.findAllByUser_Id(1L)).thenReturn(Collections.singletonList(conge));
        List<Conge> conges = congeService.getCongesByUserId(1L);
        assertFalse(conges.isEmpty());
        assertEquals(1, conges.size());
    }
}
