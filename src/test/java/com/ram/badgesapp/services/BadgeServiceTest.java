package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.BadgeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BadgeServiceTest {

    @Mock
    private BadgeRepo badgeRepo;

    @InjectMocks
    private BadgeService badgeService;

    private Badge badge;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        badge = new Badge();
        badge.setId(1L);
        badge.setUser(user);
        badge.setCode("B001");
        badge.setExpiryDate(LocalDate.now().plusYears(1));
    }

    @Test
    void getAllBadges() {
        when(badgeRepo.findAll()).thenReturn(Collections.singletonList(badge));
        List<Badge> badges = badgeService.getAllBadges();
        assertFalse(badges.isEmpty());
        assertEquals(1, badges.size());
    }

    @Test
    void getBadgeById() {
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(badge));
        Badge foundBadge = badgeService.getBadgeById(1L);
        assertNotNull(foundBadge);
        assertEquals(1L, foundBadge.getId());
    }

    @Test
    void getBadgeById_notFound() {
        when(badgeRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            badgeService.getBadgeById(1L);
        });
    }

    @Test
    void createBadge() {
        when(badgeRepo.save(any(Badge.class))).thenReturn(badge);
        Badge savedBadge = badgeService.createBadge(new Badge());
        assertNotNull(savedBadge);
        assertEquals(1L, savedBadge.getId());
    }

    @Test
    void deleteBadge() {
        doNothing().when(badgeRepo).deleteById(1L);
        badgeService.deleteBadge(1L);
        verify(badgeRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateBadge() {
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(badge));
        when(badgeRepo.save(any(Badge.class))).thenReturn(badge);

        Badge updatedInfo = new Badge();
        updatedInfo.setCode("B002");

        Badge updatedBadge = badgeService.updateBadge(1L, updatedInfo);

        assertNotNull(updatedBadge);
        assertEquals("B002", updatedBadge.getCode());
        verify(badgeRepo).save(badge);
    }

    @Test
    void updateBadge_notFound() {
        when(badgeRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            badgeService.updateBadge(1L, new Badge());
        });
    }

    @Test
    void updateExpiryDate() {
        LocalDate newExpiryDate = LocalDate.now().plusYears(2);
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(badge));
        when(badgeRepo.save(any(Badge.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Badge updatedInfo = new Badge();
        updatedInfo.setExpiryDate(newExpiryDate);

        Badge updatedBadge = badgeService.updateExpiryDate(1L, updatedInfo);

        assertNotNull(updatedBadge);
        assertEquals(newExpiryDate, updatedBadge.getExpiryDate());
        verify(badgeRepo).save(badge);
    }

    @Test
    void updateExpiryDate_notFound() {
        when(badgeRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            badgeService.updateExpiryDate(1L, new Badge());
        });
    }

    @Test
    void getBadgesByUserId() {
        when(badgeRepo.findAllByUser_Id(1L)).thenReturn(Collections.singletonList(badge));
        List<Badge> badges = badgeService.getBadgesByUserId(1L);
        assertFalse(badges.isEmpty());
        assertEquals(1, badges.size());
    }
}
