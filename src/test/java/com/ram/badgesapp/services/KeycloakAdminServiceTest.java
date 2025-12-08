package com.ram.badgesapp.services;

import com.ram.badgesapp.config.KeycloakAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakAdminServiceTest {

    @InjectMocks
    private KeycloakAdminService keycloakAdminService;

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private UserResource userResource;

    @Mock
    private RolesResource rolesResource;

    @Mock
    private RoleResource roleResource;

    @Mock
    private RoleMappingResource roleMappingResource;

    @Mock
    private RoleScopeResource roleScopeResource;


    @BeforeEach
    void setUp() {
        // Inject the mocked Keycloak instance into the service, bypassing @PostConstruct logic
        ReflectionTestUtils.setField(keycloakAdminService, "keycloak", keycloak);

        // Use lenient stubbing for mocks that are not used in every test method.
        lenient().when(keycloak.realm(anyString())).thenReturn(realmResource);
        lenient().when(realmResource.users()).thenReturn(usersResource);
        lenient().when(realmResource.roles()).thenReturn(rolesResource);
    }

    @Test
    void changePassword_success() {
        String keycloakId = "some-id";
        String newPassword = "newPass";

        when(usersResource.get(keycloakId)).thenReturn(userResource);
        doNothing().when(userResource).resetPassword(any(CredentialRepresentation.class));

        assertDoesNotThrow(() -> keycloakAdminService.changePassword(keycloakId, newPassword));

        verify(userResource).resetPassword(any(CredentialRepresentation.class));
    }

    @Test
    void changePassword_userNotFound() {
        String keycloakId = "non-existent-id";
        String newPassword = "newPass";

        when(usersResource.get(keycloakId)).thenReturn(null);

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                keycloakAdminService.changePassword(keycloakId, newPassword));

        assertEquals("User not found in Keycloak", thrown.getReason());
        verify(usersResource).get(keycloakId);
        verify(userResource, never()).resetPassword(any(CredentialRepresentation.class));
    }

    @Test
    void getUserById_success() {
        String userId = "user123";
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(userId);
        userRep.setUsername("testuser");

        when(usersResource.get(userId)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRep);

        UserRepresentation result = keycloakAdminService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(usersResource).get(userId);
        verify(userResource).toRepresentation();
    }

    @Test
    void getUserById_userNotFound() {
        String userId = "nonexistent";

        when(usersResource.get(userId)).thenThrow(new jakarta.ws.rs.NotFoundException());

        UserRepresentation result = keycloakAdminService.getUserById(userId);

        assertNull(result);
        verify(usersResource).get(userId);
    }

    @Test
    void getAllUsers_success() {
        List<UserRepresentation> mockUsers = new ArrayList<>();
        UserRepresentation user1 = new UserRepresentation();
        user1.setId("1");
        user1.setUsername("user1");
        mockUsers.add(user1);

        when(usersResource.list()).thenReturn(mockUsers);

        List<UserRepresentation> result = keycloakAdminService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(usersResource).list();
    }

    @Test
    void getAllUsers_exception() {
        when(usersResource.list()).thenThrow(new RuntimeException("Keycloak error"));

        List<UserRepresentation> result = keycloakAdminService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usersResource).list();
    }

    @Test
    void getUserRoles_success() {
        String userId = "user123";
        List<RoleRepresentation> mockRoles = new ArrayList<>();
        RoleRepresentation role1 = new RoleRepresentation();
        role1.setName("USER");
        mockRoles.add(role1);

        when(usersResource.get(userId)).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        when(roleScopeResource.listAll()).thenReturn(mockRoles);

        List<String> result = keycloakAdminService.getUserRoles(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("USER", result.get(0));
    }

    @Test
    void getUserRoles_exception() {
        String userId = "user123";
        when(usersResource.get(userId)).thenThrow(new RuntimeException("Error getting roles"));

        List<String> result = keycloakAdminService.getUserRoles(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findUsersByRole_success() {
        String roleName = "TEST_ROLE";
        Set<UserRepresentation> mockUsers = Set.of(new UserRepresentation());
        
        when(rolesResource.get(roleName)).thenReturn(roleResource);
        when(roleResource.getRoleUserMembers()).thenReturn(mockUsers);

        List<UserRepresentation> result = keycloakAdminService.findUsersByRole(roleName);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findUsersByRole_roleNotFound() {
        String roleName = "NON_EXISTENT_ROLE";

        when(rolesResource.get(roleName)).thenThrow(new jakarta.ws.rs.NotFoundException());

        List<UserRepresentation> result = keycloakAdminService.findUsersByRole(roleName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
