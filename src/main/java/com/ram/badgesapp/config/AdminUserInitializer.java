package com.ram.badgesapp.config;

import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.config.KeycloakAdminService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final KeycloakAdminService keycloakAdminService;
    private final UserEntityRepo userEntityRepo;

    public AdminUserInitializer(KeycloakAdminService keycloakAdminService, UserEntityRepo userEntityRepo) {
        this.keycloakAdminService = keycloakAdminService;
        this.userEntityRepo = userEntityRepo;
    }

    @Override
    public void run(String... args) {
        log.info("Starting admin user synchronization from Keycloak...");
        try {
            List<UserRepresentation> keycloakAdmins = keycloakAdminService.findUsersByRole("ADMIN");

            for (UserRepresentation keycloakAdmin : keycloakAdmins) {
                // Check if user already exists in local DB
                UserEntity existingUser = userEntityRepo.findByKeycloakId(keycloakAdmin.getId());
                if (existingUser == null) {
                    // If not, create a new local record
                    log.info("Admin user '{}' ({}) not found locally. Creating new record.", keycloakAdmin.getUsername(), keycloakAdmin.getId());
                    UserEntity newUser = new UserEntity();
                    newUser.setKeycloakId(keycloakAdmin.getId());
                    newUser.setEmail(keycloakAdmin.getEmail());
                    newUser.setFirstName(keycloakAdmin.getFirstName());
                    newUser.setLastName(keycloakAdmin.getLastName());
                    newUser.setRole(Role.ADMIN); // Explicitly set the role
                    newUser.setMatricule(keycloakAdmin.getLastName()+Math.random()*10);

                    userEntityRepo.save(newUser);
                } else {
                    log.info("Admin user '{}' ({}) already exists locally. No action needed.", existingUser.getEmail(), existingUser.getKeycloakId());
                }
            }
            log.info("Admin user synchronization finished successfully.");
        } catch (Exception e) {
            log.error("FATAL: Failed to synchronize admin users from Keycloak on startup.", e);
        }
    }
}
