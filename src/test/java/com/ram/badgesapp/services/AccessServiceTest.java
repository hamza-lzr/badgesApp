package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.AccessRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessServiceTest {

    @Mock
    private AccessRepo accessRepo;

    @InjectMocks
    private AccessService accessService;

    private Access access;
    private Badge badge;
    private Airport airport;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        badge = new Badge();
        badge.setId(1L);
        badge.setUser(user);

        airport = new Airport();
        airport.setId(1L);

        access = new Access();
        access.setId(1L);
        access.setBadge(badge);
        access.setAirport(airport);
    }

    @Test
    void addAccess() {
        when(accessRepo.save(any(Access.class))).thenReturn(access);
        Access savedAccess = accessService.addAccess(new Access());
        assertNotNull(savedAccess);
        assertEquals(1L, savedAccess.getId());
    }

    @Test
    void removeAccess() {
        doNothing().when(accessRepo).deleteById(1L);
        accessService.removeAccess(1L);
        verify(accessRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateAccess() {
        when(accessRepo.findById(1L)).thenReturn(Optional.of(access));
        when(accessRepo.save(any(Access.class))).thenReturn(access);

        Access updatedInfo = new Access();
        updatedInfo.setStartDate(null);
        updatedInfo.setEndDate(null);

        Access updatedAccess = accessService.updateAccess(1L, updatedInfo);

        assertNotNull(updatedAccess);
        verify(accessRepo).save(access);
    }

    @Test
    void updateAccess_notFound() {
        when(accessRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            accessService.updateAccess(1L, new Access());
        });
    }

    @Test
    void getAccessById() {
        when(accessRepo.findById(1L)).thenReturn(Optional.of(access));
        Access foundAccess = accessService.getAccessById(1L);
        assertNotNull(foundAccess);
        assertEquals(1L, foundAccess.getId());
    }

    @Test
    void getAccessById_notFound() {
        when(accessRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            accessService.getAccessById(1L);
        });
    }

    @Test
    void getAccessesByBadgeId() {
        when(accessRepo.findAllByBadge_Id(1L)).thenReturn(Collections.singletonList(access));
        List<Access> accesses = accessService.getAccessesByBadgeId(1L);
        assertFalse(accesses.isEmpty());
        assertEquals(1, accesses.size());
    }

    @Test
    void getAccessesByAirportId() {
        when(accessRepo.findAllByAirport_Id(1L)).thenReturn(Collections.singletonList(access));
        List<Access> accesses = accessService.getAccessesByAirportId(1L);
        assertFalse(accesses.isEmpty());
        assertEquals(1, accesses.size());
    }

    @Test
    void getAllAccesses() {
        when(accessRepo.findAll()).thenReturn(Collections.singletonList(access));
        List<Access> accesses = accessService.getAllAccesses();
        assertFalse(accesses.isEmpty());
        assertEquals(1, accesses.size());
    }

    @Test
    void getAccessesByUserId() {
        when(accessRepo.findAllByBadge_User_Id(1L)).thenReturn(Collections.singletonList(access));
        List<Access> accesses = accessService.getAccessesByUserId(1L);
        assertFalse(accesses.isEmpty());
        assertEquals(1, accesses.size());
    }
}
