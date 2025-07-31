package com.ram.badgesapp.config;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class KeycloakAdminService {

    private final Keycloak keycloak;
    private final String targetRealm = "ram"; // realm where you create users

    public KeycloakAdminService() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8081")
                .realm("master") // ✅ must authenticate in master
                .clientId("ram-admin") // ✅ admin client created in master
                .clientSecret("90veClOzMBtB48mnhtQfDqodjh6fAa89")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public String createKeycloakUser(String username,
                                     String email,
                                     String password,
                                     String roleName,
                                     String firstName,
                                     String lastName) {

        // 1️⃣ Create user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        // 2️⃣ Create user in target realm
        Response response = keycloak.realm(targetRealm).users().create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create Keycloak user: " + response.getStatusInfo());
        }

        // 3️⃣ Get Keycloak UUID (userId)
        String userId = CreatedResponseUtil.getCreatedId(response);

        // 4️⃣ Set initial password
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        keycloak.realm(targetRealm).users().get(userId).resetPassword(credentials);

        // 5️⃣ Assign realm role
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

        return userId; // UUID of created user
    }
    
    /**
     * Changes the password for a Keycloak user
     * 
     * @param keycloakId The Keycloak UUID of the user
     * @param newPassword The new password to set
     * @throws ResponseStatusException if the user is not found or the password change fails
     */
    public void changePassword(String keycloakId, String newPassword) {
        try {
            // Create credential representation
            CredentialRepresentation credentials = new CredentialRepresentation();
            credentials.setTemporary(false);
            credentials.setType(CredentialRepresentation.PASSWORD);
            credentials.setValue(newPassword);
            
            // Get user resource and reset password
            UserResource userResource = keycloak.realm(targetRealm).users().get(keycloakId);
            if (userResource == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in Keycloak");
            }
            
            userResource.resetPassword(credentials);
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            }
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to change password: " + e.getMessage(), 
                e
            );
        }
    }
}



