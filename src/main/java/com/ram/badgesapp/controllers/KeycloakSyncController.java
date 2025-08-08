package com.ram.badgesapp.controllers;

import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.services.KeycloakSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/keycloak-sync")
public class KeycloakSyncController {

    private final KeycloakSyncService keycloakSyncService;

    public KeycloakSyncController(KeycloakSyncService keycloakSyncService) {
        this.keycloakSyncService = keycloakSyncService;
    }

    /**
     * Endpoint to synchronize a specific Keycloak user with the application database.
     * This is useful when you've manually created a user in Keycloak and want to add them to the app database.
     * 
     * @param keycloakId The Keycloak ID of the user to synchronize
     * @return The synchronized user entity
     */
    @PostMapping("/user/{keycloakId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserEntity> syncUser(@PathVariable String keycloakId) {
        UserEntity syncedUser = keycloakSyncService.syncKeycloakUser(keycloakId);
        return ResponseEntity.ok(syncedUser);
    }

    /**
     * Endpoint to synchronize all Keycloak users with the application database.
     * This will create or update user entries in the database for all users in Keycloak.
     * 
     * @return The number of users synchronized
     */
    @PostMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> syncAllUsers() {
        int syncCount = keycloakSyncService.syncAllKeycloakUsers();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Synchronization completed successfully");
        response.put("syncedUsers", syncCount);
        
        return ResponseEntity.ok(response);
    }
}