package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.UserDTO;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.UserMapper;
import com.ram.badgesapp.services.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.ram.badgesapp.config.KeycloakAdminService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final UserMapper userMapper;
    private final KeycloakAdminService keycloakAdminService;
    public UserEntityController(UserEntityService userEntityService, UserMapper userMapper, KeycloakAdminService keycloakAdminService) {
        this.userEntityService = userEntityService;
        this.userMapper = userMapper;
        this.keycloakAdminService = keycloakAdminService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userEntityService.getAllUsers().stream()
                .map(userMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.canAccessEmployee(authentication, #id)")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDTO(userEntityService.getUserById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userEntityService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully with id: " + id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

        // Map DTO to Entity
        UserEntity userEntity = userMapper.toEntity(userDTO);

        // For ADMIN users, generate a default password; for EMPLOYEE users, set password to null
        String initialPassword = null;
        if ("ADMIN".equals(userEntity.getRole().name())) {
            initialPassword = "changeme123"; // Default password for ADMIN users
        }
        // For EMPLOYEE users, password remains null and they'll receive an email to set it

        // ✅ Create user in Keycloak and get the UUID
        String keycloakId = keycloakAdminService.createKeycloakUser(
                userEntity.getEmail(),          // username/email
                userEntity.getEmail(),          // email
                initialPassword,                // password (null for EMPLOYEE)
                userEntity.getRole().name(),    // Role: ADMIN or EMPLOYEE
                userEntity.getFirstName(),
                userEntity.getLastName()
        );

        // ✅ Save Keycloak ID in DB
        userEntity.setKeycloakId(keycloakId);

        // Save DB record
        UserEntity saved = userEntityService.createUser(userEntity);

        // Return DTO
        return ResponseEntity.ok(userMapper.toDTO(saved));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserEntity user = userMapper.toEntity(userDTO);
        UserEntity updated = userEntityService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserStatusById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserEntity user = userMapper.toEntity(userDTO);
        UserEntity updated = userEntityService.updateEmployeeStatus(id, user);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }
    @DeleteMapping("/{id}/badge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> removeBadgeFromUserById(@PathVariable Long id) {
        UserEntity updated = userEntityService.removeBadgeFromEmployee(id);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }
    @PutMapping("/{id}/badge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addOrUpdateUserBadgeById(@PathVariable Long id, @RequestBody Long idBadge) {
        UserEntity updated = userEntityService.addOrUpdateEmployeeBadge(id, idBadge);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @PutMapping("/{id}/company/{idComp}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addOrUpdateUserCompanyById(@PathVariable Long id, @PathVariable Long idComp){
        UserEntity updated = userEntityService.addOrUpdateEmployeeCompany(id, idComp);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @GetMapping("/by-keycloak/{id}")
    public ResponseEntity<UserDTO> getUserByKeycloakId(@PathVariable String id) {
        return ResponseEntity.ok(userMapper.toDTO(userEntityService.getUserByKeycloakId(id)));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        UserEntity user = userEntityService.getUserByKeycloakId(keycloakId);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }
    
    /**
     * Endpoint for changing the current user's password
     * 
     * @param auth The JWT authentication token
     * @param passwordData Map containing the new password
     * @return ResponseEntity with success message
     */
    @PostMapping("/change-password")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> changePassword(
            JwtAuthenticationToken auth,
            @RequestBody Map<String, String> passwordData) {
        
        // Extract Keycloak ID from JWT token
        String keycloakId = auth.getToken().getSubject();
        
        // Validate password data
        String newPassword = passwordData.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "New password is required"));
        }
        
        // Minimum password requirements check
        if (newPassword.length() < 8) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password must be at least 8 characters long"));
        }
        
        // Change password in Keycloak
        keycloakAdminService.changePassword(keycloakId, newPassword);
        
        // Return success response
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }







}
