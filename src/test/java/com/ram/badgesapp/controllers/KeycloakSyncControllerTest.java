package com.ram.badgesapp.controllers;

import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.services.KeycloakSyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(KeycloakSyncController.class)
public class KeycloakSyncControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KeycloakSyncService keycloakSyncService;

    @Test
    public void syncUser_shouldSyncUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setKeycloakId("keycloak-id");

        when(keycloakSyncService.syncKeycloakUser("keycloak-id")).thenReturn(user);

        mockMvc.perform(post("/api/keycloak-sync/user/keycloak-id")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.keycloakId", is("keycloak-id")));
    }

    @Test
    public void syncAllUsers_shouldSyncAllUsers() throws Exception {
        when(keycloakSyncService.syncAllKeycloakUsers()).thenReturn(10);

        mockMvc.perform(post("/api/keycloak-sync/all")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Synchronization completed successfully")))
                .andExpect(jsonPath("$.syncedUsers", is(10)));
    }
}
