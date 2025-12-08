package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationMapperHelperTest {

    @Mock
    private UserEntityRepo userRepo;

    @InjectMocks
    private NotificationMapperHelper notificationMapperHelper;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
    }

    @Test
    void userFromId_validId_returnsUser() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        UserEntity result = notificationMapperHelper.userFromId(1L);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    void userFromId_nullId_returnsNull() {
        UserEntity result = notificationMapperHelper.userFromId(null);
        assertNull(result);
        verify(userRepo, never()).findById(anyLong());
    }

    @Test
    void userFromId_userNotFound_throwsRuntimeException() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> notificationMapperHelper.userFromId(99L));
        assertEquals("User not found with id: 99", exception.getMessage());
        verify(userRepo, times(1)).findById(99L);
    }
}
