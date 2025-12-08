package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.NotificationDTO;
import com.ram.badgesapp.entities.Notification;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class NotificationMapperTest {

    @Autowired
    private NotificationMapper notificationMapper;

    @MockitoBean
    private UserEntityRepo userEntityRepo;

    private Notification notification;
    private NotificationDTO notificationDTO;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        // For toDTO test
        notification = new Notification();
        notification.setId(10L);
        notification.setUser(user);
        notification.setMessage("Test message");
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now().minusDays(1));

        // For toEntity test
        notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(1L);
        notificationDTO.setMessage("DTO message");
        notificationDTO.setRead(true);
        // id and createdAt are set by mapper
    }

    @Test
    void testToDTO() {
        NotificationDTO resultDTO = notificationMapper.toDTO(notification);

        assertNotNull(resultDTO);
        assertEquals(notification.getId(), resultDTO.getId());
        assertEquals(notification.getUser().getId(), resultDTO.getUserId());
        assertEquals(notification.getMessage(), resultDTO.getMessage());
        assertEquals(notification.isRead(), resultDTO.isRead());
        assertEquals(notification.getCreatedAt(), resultDTO.getCreatedAt());
    }

    @Test
    void testToEntity() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));

        // Capture time before mapping to check the auto-generated timestamp
        LocalDateTime beforeMapping = LocalDateTime.now();

        Notification resultEntity = notificationMapper.toEntity(notificationDTO);

        assertNotNull(resultEntity);
        assertNull(resultEntity.getId()); // ID should be ignored
        assertEquals(user, resultEntity.getUser());
        assertEquals(notificationDTO.getMessage(), resultEntity.getMessage());
        assertEquals(notificationDTO.isRead(), resultEntity.isRead());
        assertNotNull(resultEntity.getCreatedAt());
        assertTrue(resultEntity.getCreatedAt().isAfter(beforeMapping.minusSeconds(1)));
        assertTrue(resultEntity.getCreatedAt().isBefore(beforeMapping.plusSeconds(1)));
    }
}
