package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.NotificationDTO;
import com.ram.badgesapp.mapper.NotificationMapper;
import com.ram.badgesapp.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
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

    @PatchMapping("/{id}")
    ResponseEntity<NotificationDTO> updateNotificationRead(@PathVariable Long id){
        return ResponseEntity.ok(notificationMapper.toDTO(notificationService.updateNotificationRead(id)));
    }

}
