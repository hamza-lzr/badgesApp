package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.NotificationDTO;
import com.ram.badgesapp.entities.Notification;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.NotificationMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.KeycloakSyncService;
import com.ram.badgesapp.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private NotificationMapper notificationMapper;

    @MockitoBean
    private UserEntityRepo userEntityRepo;

    @MockitoBean
    private KeycloakSyncService keycloakSyncService;

    @Test
    public void getAllNotifications_shouldReturnListOfNotifications() throws Exception {
        Notification notification = new Notification();
        notification.setId(1L);
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        when(notificationService.getAllNotifications()).thenReturn(Collections.singletonList(notification));
        when(notificationMapper.toDTO(any(Notification.class))).thenReturn(notificationDTO);

        mockMvc.perform(get("/notification")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getNotificationById_shouldReturnNotification() throws Exception {
        Notification notification = new Notification();
        notification.setId(1L);
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        when(notificationService.getNotificationById(1L)).thenReturn(notification);
        when(notificationMapper.toDTO(notification)).thenReturn(notificationDTO);

        mockMvc.perform(get("/notification/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteNotificationById_shouldDeleteNotification() throws Exception {
        mockMvc.perform(delete("/notification/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void createNotification_shouldCreateNotification() throws Exception {
        Notification notification = new Notification();
        notification.setId(1L);
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        when(notificationMapper.toEntity(any(NotificationDTO.class))).thenReturn(notification);
        when(notificationService.createNotification(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toDTO(any(Notification.class))).thenReturn(notificationDTO);

        mockMvc.perform(post("/notification")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }


    @Test
    public void getMyNotifications_shouldReturnMyNotifications() throws Exception {
        String keycloakId = "user-id";
        UserEntity user = new UserEntity();
        user.setId(1L);
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        when(keycloakSyncService.syncKeycloakUser(keycloakId)).thenReturn(user);
        when(notificationService.getNotificationsByUserId(1L)).thenReturn(Collections.singletonList(notification));
        when(notificationMapper.toDTO(any(Notification.class))).thenReturn(notificationDTO);

        mockMvc.perform(get("/notification/my")
                .with(jwt().jwt(builder -> builder.subject(keycloakId))))
                .andExpect(status().isOk());

    }


}
