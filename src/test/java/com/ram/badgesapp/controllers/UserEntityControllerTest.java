package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.config.KeycloakAdminService;
import com.ram.badgesapp.config.SecurityService;
import com.ram.badgesapp.dto.UserDTO;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.UserMapper;
import com.ram.badgesapp.services.UserEntityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserEntityController.class)
public class UserEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserEntityService userEntityService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private KeycloakAdminService keycloakAdminService;

    @MockBean
    private SecurityService securityService;

    @Test
    public void getAllUsers_shouldReturnListOfUsers() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userEntityService.getAllUsers()).thenReturn(Collections.singletonList(user));
        when(userMapper.toDTO(any(UserEntity.class))).thenReturn(userDTO);

        mockMvc.perform(get("/users")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getUserById_shouldReturnUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(securityService.canAccessEmployee(any(), any())).thenReturn(true);
        when(userEntityService.getUserById(1L)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteUserById_shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    public void createUser_shouldCreateUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setRole(com.ram.badgesapp.entities.Role.ADMIN);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(keycloakAdminService.createKeycloakUser(any(), any(), any(), any(), any(), any())).thenReturn("keycloak-id");
        when(userEntityService.createUser(any(UserEntity.class))).thenReturn(user);
        when(userMapper.toDTO(any(UserEntity.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateUserById_shouldUpdateUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userEntityService.updateUser(any(Long.class), any(UserEntity.class))).thenReturn(user);
        when(userMapper.toDTO(any(UserEntity.class))).thenReturn(userDTO);

        mockMvc.perform(put("/users/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getCurrentUser_shouldReturnCurrentUser() throws Exception {
        String keycloakId = "user-id";
        UserEntity user = new UserEntity();
        user.setId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userEntityService.getUserByKeycloakId(keycloakId)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users/me")
                .with(jwt().jwt(builder -> builder.subject(keycloakId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void changePassword_shouldChangePassword() throws Exception {
        String keycloakId = "user-id";
        Map<String, String> passwordData = Map.of("newPassword", "newPassword123");

        mockMvc.perform(post("/users/change-password")
                .with(jwt().jwt(builder -> builder.subject(keycloakId)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Password changed successfully")));
    }
}
