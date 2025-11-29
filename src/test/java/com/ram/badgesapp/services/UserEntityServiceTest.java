package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @Mock
    private UserEntityRepo userEntityRepo;

    @Mock
    private BadgeRepo badgeRepo;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private UserEntityService userEntityService;

    private UserEntity user;
    private Badge badge;
    private Company company;
    private JwtAuthenticationToken jwtAuthenticationToken;
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setKeycloakId("keycloak-id");
        user.setFirstName("Test");
        user.setLastName("User");

        badge = new Badge();
        badge.setId(1L);
        
        company = new Company();
        company.setId(1L);

        Map<String, Object> claims = Map.of(
                "sub", "keycloak-id",
                "email", "test@example.com",
                "given_name", "Test",
                "family_name", "User",
                "realm_access", Map.of("roles", List.of("EMPLOYEE"))
        );
        jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), claims);
        jwtAuthenticationToken = new JwtAuthenticationToken(jwt);
    }

    @Test
    void createUser() {
        when(userEntityRepo.save(any(UserEntity.class))).thenReturn(user);
        UserEntity savedUser = userEntityService.createUser(new UserEntity());
        assertNotNull(savedUser);
    }

    @Test
    void getAllUsers() {
        when(userEntityRepo.findAll()).thenReturn(Collections.singletonList(user));
        List<UserEntity> users = userEntityService.getAllUsers();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void getUserById() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));
        UserEntity foundUser = userEntityService.getUserById(1L);
        assertNotNull(foundUser);
    }

    @Test
    void deleteUser() {
        doNothing().when(userEntityRepo).deleteById(1L);
        userEntityService.deleteUser(1L);
        verify(userEntityRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateUser() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userEntityRepo.save(any(UserEntity.class))).thenReturn(user);
        UserEntity updated = userEntityService.updateUser(1L, new UserEntity());
        assertNotNull(updated);
    }

    @Test
    void updateEmployeeStatus() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userEntityRepo.save(any(UserEntity.class))).thenReturn(user);
        UserEntity updated = userEntityService.updateEmployeeStatus(1L, new UserEntity());
        assertNotNull(updated);
    }

    @Test
    void removeBadgeFromEmployee() {
        user.getBadge().add(badge);
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userEntityRepo.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserEntity updatedUser = userEntityService.removeBadgeFromEmployee(1L);
        assertNull(updatedUser.getBadge());
    }

    @Test
    void addOrUpdateEmployeeBadge() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(badge));
        when(userEntityRepo.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserEntity updatedUser = userEntityService.addOrUpdateEmployeeBadge(1L, 1L);
        assertTrue(updatedUser.getBadge().contains(badge));
    }
    
    @Test
    void addOrUpdateEmployeeBadge_userNotFound() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userEntityService.addOrUpdateEmployeeBadge(1L, 1L));
    }


    @Test
    void addOrUpdateEmployeeCompany() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));
        when(companyService.getCompanyById(1L)).thenReturn(company);
        when(userEntityRepo.save(any(UserEntity.class))).thenReturn(user);

        UserEntity updatedUser = userEntityService.addOrUpdateEmployeeCompany(1L, 1L);
        assertEquals(company, updatedUser.getCompany());
    }

    @Test
    void getUserByKeycloakId() {
        when(userEntityRepo.findByKeycloakId("keycloak-id")).thenReturn(user);
        UserEntity foundUser = userEntityService.getUserByKeycloakId("keycloak-id");
        assertNotNull(foundUser);
    }

    @Test
    void getCurrentEmployee_existingUser() {
        when(userEntityRepo.findByKeycloakId("keycloak-id")).thenReturn(user);
        UserEntity currentUser = userEntityService.getCurrentEmployee(jwtAuthenticationToken);
        assertEquals(user, currentUser);
    }

    @Test
    void getCurrentEmployee_newUser() {
        when(userEntityRepo.findByKeycloakId("keycloak-id")).thenReturn(null);
        when(userEntityRepo.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));
        UserEntity newUser = userEntityService.getCurrentEmployee(jwtAuthenticationToken);
        assertEquals("keycloak-id", newUser.getKeycloakId());
        assertEquals(Role.EMPLOYEE, newUser.getRole());
    }

    @Test
    void canAccessEmployee_asAdmin() {
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication adminAuth = new JwtAuthenticationToken(jwt, authorities);
        assertTrue(userEntityService.canAccessEmployee(adminAuth, 2L)); // Admin can access another user
    }

    @Test
    void canAccessEmployee_asSelf() {
        when(userEntityRepo.findByKeycloakId("keycloak-id")).thenReturn(user);
        assertTrue(userEntityService.canAccessEmployee(jwtAuthenticationToken, 1L));
    }
    
    @Test
    void canAccessEmployee_asOtherEmployee() {
        when(userEntityRepo.findByKeycloakId("keycloak-id")).thenReturn(user);
        assertFalse(userEntityService.canAccessEmployee(jwtAuthenticationToken, 2L));
    }

    @Test
    void ensureAdminExists_doesNotExist() {
        when(userEntityRepo.findByKeycloakId("admin-id")).thenReturn(null);
        userEntityService.ensureAdminExists("admin-id");
        verify(userEntityRepo, times(1)).save(any(UserEntity.class));
    }
    
    @Test
    void ensureAdminExists_alreadyExists() {
        when(userEntityRepo.findByKeycloakId("admin-id")).thenReturn(user);
        userEntityService.ensureAdminExists("admin-id");
        verify(userEntityRepo, never()).save(any(UserEntity.class));
    }
}
