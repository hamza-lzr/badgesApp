package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.UserDTO;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.UserMapper;
import com.ram.badgesapp.services.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ram.badgesapp.config.KeycloakAdminService;

import java.util.List;

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

        // Generate a default password (or get from DTO)
        //String initialPassword = "changeme123";

        // ✅ Create user in Keycloak and get the UUID
        String keycloakId = keycloakAdminService.createKeycloakUser(
                userEntity.getEmail(),          // username/email
                userEntity.getEmail(),          // email
                userEntity.getMatricule(),
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
}
