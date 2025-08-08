package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.NotificationDTO;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.NotificationMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.KeycloakSyncService;
import com.ram.badgesapp.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final UserEntityRepo userEntityRepo;
    private final KeycloakSyncService keycloakSyncService;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper, UserEntityRepo userEntityRepo,  KeycloakSyncService keycloakSyncService) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.userEntityRepo = userEntityRepo;
        this.keycloakSyncService = keycloakSyncService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications().stream()
                .map(notificationMapper::toDTO)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationMapper.toDTO(notificationService.getNotificationById(id)));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteNotificationById(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification deleted successfully with id: " + id);
    }

    @PostMapping
    ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        NotificationDTO saved = notificationMapper.toDTO(notificationService.createNotification(notificationMapper.toEntity(notificationDTO)));
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user-id/{id}")
    ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable Long idUser) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(idUser).stream()
                .map(notificationMapper::toDTO)
                .toList());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public List<NotificationDTO> getMyNotifications(@AuthenticationPrincipal Jwt jwt) {

        // ✅ Extract Keycloak UUID from token
        String keycloakUserId = jwt.getSubject();

        // ✅ Map Keycloak UUID → internal UserEntity.id
        UserEntity user = keycloakSyncService.syncKeycloakUser(keycloakUserId); // 🔁 sync auto si inexistant
        Long internalUserId = user.getId();

        // ✅ Fetch & return only that user's notifications
        return notificationService.getNotificationsByUserId(internalUserId)
                .stream()
                .map(notification -> {
                    NotificationDTO dto = new NotificationDTO();
                    dto.setId(notification.getId());
                    dto.setMessage(notification.getMessage());
                    dto.setRead(notification.isRead());
                    dto.setCreatedAt(notification.getCreatedAt());
                    dto.setUserId(notification.getUser().getId());
                    return dto;
                })
                .toList();
    }

    @PutMapping("/mark-all-read")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal Jwt jwt) {

        // Get the Keycloak user ID (UUID)
        String keycloakUserId = jwt.getSubject();

        // Map Keycloak UUID -> your internal userId
        Long internalUserId = userEntityRepo.findByKeycloakId(keycloakUserId)
                .getId();

        // Update all notifications for this user
        notificationService.markAllAsReadForUser(internalUserId);

        return ResponseEntity.ok().build();
    }



    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public NotificationDTO markAsRead(@PathVariable Long id) {
        return notificationMapper.toDTO(notificationService.updateNotificationRead(id));
    }


    @PatchMapping("/{id}")
    ResponseEntity<NotificationDTO> updateNotificationRead(@PathVariable Long id){
        return ResponseEntity.ok(notificationMapper.toDTO(notificationService.updateNotificationRead(id)));
    }

}
