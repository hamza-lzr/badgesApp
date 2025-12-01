package com.ram.badgesapp.config;

import jakarta.annotation.PostConstruct; // 1. Add this import
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminService {

    // 2. Remove 'final'. We initialize it later in @PostConstruct.
    private Keycloak keycloak;

    private final String targetRealm = "ram";

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    // 3. Keep the constructor empty.
    // Do not put logic here, because clientSecret is still null!
    public KeycloakAdminService() {
    }

    // 4. Move initialization here. This runs automatically AFTER injection.
    @PostConstruct
    public void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8081")
                .realm("master")
                .clientId("ram-admin")
                .clientSecret(clientSecret) // Now this is safe to use
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public String createKeycloakUser(String username, String email, String password, String roleName, String firstName, String lastName) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        Response response = keycloak.realm(targetRealm).users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create Keycloak user: " + response.getStatusInfo());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        if (!"EMPLOYEE".equals(roleName) && password != null && !password.isEmpty()) {
            CredentialRepresentation credentials = new CredentialRepresentation();
            credentials.setTemporary(false);
            credentials.setType(CredentialRepresentation.PASSWORD);
            credentials.setValue(password);
            keycloak.realm(targetRealm).users().get(userId).resetPassword(credentials);
        }

        RoleRepresentation realmRole = keycloak.realm(targetRealm)
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(targetRealm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(realmRole));

        if ("EMPLOYEE".equals(roleName)) {
            keycloak.realm(targetRealm)
                    .users()
                    .get(userId)
                    .executeActionsEmail(List.of("VERIFY_EMAIL", "UPDATE_PASSWORD"));
        }

        return userId;
    }

    public void changePassword(String keycloakId, String newPassword) {
        try {
            CredentialRepresentation credentials = new CredentialRepresentation();
            credentials.setTemporary(false);
            credentials.setType(CredentialRepresentation.PASSWORD);
            credentials.setValue(newPassword);

            UserResource userResource = keycloak.realm(targetRealm).users().get(keycloakId);
            if (userResource == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in Keycloak");
            }

            userResource.resetPassword(credentials);
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to change password: " + e.getMessage(), e);
        }
    }

    public UserRepresentation getUserById(String userId) {
        try {
            return keycloak.realm(targetRealm).users().get(userId).toRepresentation();
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserRepresentation> getAllUsers() {
        try {
            return keycloak.realm(targetRealm).users().list();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String> getUserRoles(String userId) {
        try {
            List<RoleRepresentation> roles = keycloak.realm(targetRealm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .listAll();

            return roles.stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<UserRepresentation> findUsersByRole(String roleName) {
        try {
            return keycloak.realm(targetRealm)
                    .roles()
                    .get(roleName)
                    .getRoleUserMembers()
                    .stream().toList();
        } catch (NotFoundException e) {
            return List.of();
        }
    }
}