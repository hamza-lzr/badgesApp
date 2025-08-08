package com.ram.badgesapp.services;

import com.ram.badgesapp.config.KeycloakAdminService;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakSyncService {

    private final KeycloakAdminService keycloakAdminService;
    private final UserEntityRepo userEntityRepo;

    public KeycloakSyncService(KeycloakAdminService keycloakAdminService, UserEntityRepo userEntityRepo) {
        this.keycloakAdminService = keycloakAdminService;
        this.userEntityRepo = userEntityRepo;
    }

    /**
     * Synchronizes a specific Keycloak user with the application database.
     * If the user already exists in the database, it will be updated with the latest information from Keycloak.
     * If the user doesn't exist in the database, a new entry will be created.
     *
     * @param keycloakId The Keycloak ID of the user to synchronize
     * @return The synchronized UserEntity
     */
    public UserEntity syncKeycloakUser(String keycloakId) {
        // Get user from Keycloak
        UserRepresentation keycloakUser = keycloakAdminService.getUserById(keycloakId);
        
        if (keycloakUser == null) {
            throw new RuntimeException("User not found in Keycloak with ID: " + keycloakId);
        }
        
        // Check if user already exists in our database
        UserEntity userEntity = userEntityRepo.findByKeycloakId(keycloakId);
        
        if (userEntity == null) {
            // Create new user entity
            userEntity = new UserEntity();
            userEntity.setKeycloakId(keycloakId);
        }
        
        // Update user information from Keycloak
        userEntity.setEmail(keycloakUser.getEmail());
        userEntity.setFirstName(keycloakUser.getFirstName());
        userEntity.setLastName(keycloakUser.getLastName());
        
        // Handle required matricule field if it's not set
        if (userEntity.getMatricule() == null) {
            // Generate a matricule based on username or email if not available
            String username = keycloakUser.getUsername();
            String email = keycloakUser.getEmail();
            String baseForMatricule = username != null ? username : email.split("@")[0];
            
            // Create a unique matricule by adding a timestamp
            String matricule = baseForMatricule + "-" + System.currentTimeMillis();
            userEntity.setMatricule(matricule);
        }
        
        // Get user roles from Keycloak
        List<String> roles = keycloakAdminService.getUserRoles(keycloakId);
        
        // Set role based on Keycloak roles
        if (roles.contains("ADMIN")) {
            userEntity.setRole(Role.ADMIN);
        } else {
            userEntity.setRole(Role.EMPLOYEE);
        }
        
        // Save user to database
        return userEntityRepo.save(userEntity);
    }

    /**
     * Synchronizes all users from Keycloak with the application database.
     * This method will create or update user entries in the database based on Keycloak users.
     *
     * @return The number of users synchronized
     */
    public int syncAllKeycloakUsers() {
        // Get all users from Keycloak
        List<UserRepresentation> keycloakUsers = keycloakAdminService.getAllUsers();
        
        int syncCount = 0;
        
        for (UserRepresentation keycloakUser : keycloakUsers) {
            try {
                syncKeycloakUser(keycloakUser.getId());
                syncCount++;
            } catch (Exception e) {
                // Log error but continue with next user
                System.err.println("Error synchronizing user " + keycloakUser.getUsername() + ": " + e.getMessage());
            }
        }
        
        return syncCount;
    }
}