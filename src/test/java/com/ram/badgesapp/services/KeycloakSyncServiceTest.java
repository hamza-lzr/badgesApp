package com.ram.badgesapp.services;

import com.ram.badgesapp.config.KeycloakAdminService;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakSyncServiceTest {

    @Mock
    private KeycloakAdminService keycloakAdminService;

    @Mock
    private UserEntityRepo userEntityRepo;

    @InjectMocks
    private KeycloakSyncService keycloakSyncService;

    private UserRepresentation adminUser;
    private UserRepresentation employeeUser;

    @BeforeEach
    void setUp() {
        // Setup admin user
        adminUser = new UserRepresentation();
        adminUser.setId("admin-keycloak-id");
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        
        // Setup employee user
        employeeUser = new UserRepresentation();
        employeeUser.setId("employee-keycloak-id");
        employeeUser.setUsername("employee");
        employeeUser.setEmail("employee@example.com");
        employeeUser.setFirstName("Employee");
        employeeUser.setLastName("User");
    }

    @Test
    void syncKeycloakUser_NewAdminUser_CreatesUserWithAdminRole() {
        // Arrange
        when(keycloakAdminService.getUserById("admin-keycloak-id")).thenReturn(adminUser);
        when(keycloakAdminService.getUserRoles("admin-keycloak-id")).thenReturn(Arrays.asList("ADMIN", "USER"));
        when(userEntityRepo.findByKeycloakId("admin-keycloak-id")).thenReturn(null);
        
        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setKeycloakId("admin-keycloak-id");
        savedUser.setEmail("admin@example.com");
        savedUser.setFirstName("Admin");
        savedUser.setLastName("User");
        savedUser.setRole(Role.ADMIN);
        savedUser.setMatricule("admin-" + System.currentTimeMillis());
        
        when(userEntityRepo.save(any(UserEntity.class))).thenReturn(savedUser);
        
        // Act
        UserEntity result = keycloakSyncService.syncKeycloakUser("admin-keycloak-id");
        
        // Assert
        assertNotNull(result);
        assertEquals("admin-keycloak-id", result.getKeycloakId());
        assertEquals("admin@example.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
        assertNotNull(result.getMatricule());
        
        verify(keycloakAdminService).getUserById("admin-keycloak-id");
        verify(keycloakAdminService).getUserRoles("admin-keycloak-id");
        verify(userEntityRepo).findByKeycloakId("admin-keycloak-id");
        verify(userEntityRepo).save(any(UserEntity.class));
    }

    @Test
    void syncKeycloakUser_ExistingUser_UpdatesUserInfo() {
        // Arrange
        when(keycloakAdminService.getUserById("employee-keycloak-id")).thenReturn(employeeUser);
        when(keycloakAdminService.getUserRoles("employee-keycloak-id")).thenReturn(Collections.singletonList("EMPLOYEE"));
        
        UserEntity existingUser = new UserEntity();
        existingUser.setId(2L);
        existingUser.setKeycloakId("employee-keycloak-id");
        existingUser.setEmail("old-email@example.com");
        existingUser.setFirstName("Old");
        existingUser.setLastName("Name");
        existingUser.setRole(Role.EMPLOYEE);
        existingUser.setMatricule("EMP001");
        
        when(userEntityRepo.findByKeycloakId("employee-keycloak-id")).thenReturn(existingUser);
        
        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(2L);
        updatedUser.setKeycloakId("employee-keycloak-id");
        updatedUser.setEmail("employee@example.com");
        updatedUser.setFirstName("Employee");
        updatedUser.setLastName("User");
        updatedUser.setRole(Role.EMPLOYEE);
        updatedUser.setMatricule("EMP001");
        
        when(userEntityRepo.save(any(UserEntity.class))).thenReturn(updatedUser);
        
        // Act
        UserEntity result = keycloakSyncService.syncKeycloakUser("employee-keycloak-id");
        
        // Assert
        assertNotNull(result);
        assertEquals("employee-keycloak-id", result.getKeycloakId());
        assertEquals("employee@example.com", result.getEmail());
        assertEquals("Employee", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(Role.EMPLOYEE, result.getRole());
        assertEquals("EMP001", result.getMatricule());
        
        verify(keycloakAdminService).getUserById("employee-keycloak-id");
        verify(keycloakAdminService).getUserRoles("employee-keycloak-id");
        verify(userEntityRepo).findByKeycloakId("employee-keycloak-id");
        verify(userEntityRepo).save(any(UserEntity.class));
    }

    @Test
    void syncAllKeycloakUsers_MultipleUsers_SyncsAllUsers() {
        // Arrange
        when(keycloakAdminService.getAllUsers()).thenReturn(Arrays.asList(adminUser, employeeUser));
        
        // Mock successful sync for admin
        when(keycloakAdminService.getUserById("admin-keycloak-id")).thenReturn(adminUser);
        when(keycloakAdminService.getUserRoles("admin-keycloak-id")).thenReturn(Arrays.asList("ADMIN"));
        when(userEntityRepo.findByKeycloakId("admin-keycloak-id")).thenReturn(null);
        
        // Mock successful sync for employee
        when(keycloakAdminService.getUserById("employee-keycloak-id")).thenReturn(employeeUser);
        when(keycloakAdminService.getUserRoles("employee-keycloak-id")).thenReturn(Arrays.asList("EMPLOYEE"));
        when(userEntityRepo.findByKeycloakId("employee-keycloak-id")).thenReturn(null);
        
        when(userEntityRepo.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(savedUser.getKeycloakId().equals("admin-keycloak-id") ? 1L : 2L);
            return savedUser;
        });
        
        // Act
        int result = keycloakSyncService.syncAllKeycloakUsers();
        
        // Assert
        assertEquals(2, result);
        verify(keycloakAdminService).getAllUsers();
        verify(keycloakAdminService, times(2)).getUserById(anyString());
        verify(keycloakAdminService, times(2)).getUserRoles(anyString());
        verify(userEntityRepo, times(2)).findByKeycloakId(anyString());
        verify(userEntityRepo, times(2)).save(any(UserEntity.class));
    }
}