package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.AccessDTO;
import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.AccessMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.AccessService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccessController.class)
public class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccessService accessService;

    @MockBean
    private AccessMapper accessMapper;

    @MockBean
    private UserEntityRepo userEntityRepo;

    @Test
    public void getAllAccesses_shouldReturnListOfAccesses() throws Exception {
        Access access = new Access();
        access.setId(1L);
        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setId(1L);

        when(accessService.getAllAccesses()).thenReturn(Collections.singletonList(access));
        when(accessMapper.toDTO(any(Access.class))).thenReturn(accessDTO);

        mockMvc.perform(get("/access")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getAccessById_shouldReturnAccess() throws Exception {
        Access access = new Access();
        access.setId(1L);
        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setId(1L);

        when(accessService.getAccessById(1L)).thenReturn(access);
        when(accessMapper.toDTO(access)).thenReturn(accessDTO);

        mockMvc.perform(get("/access/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getAccessesByBadgeId_shouldReturnListOfAccesses() throws Exception {
        Access access = new Access();
        access.setId(1L);
        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setId(1L);

        when(accessService.getAccessesByBadgeId(1L)).thenReturn(Collections.singletonList(access));
        when(accessMapper.toDTO(any(Access.class))).thenReturn(accessDTO);

        mockMvc.perform(get("/access/badge/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getAccessesByAirportId_shouldReturnListOfAccesses() throws Exception {
        Access access = new Access();
        access.setId(1L);
        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setId(1L);

        when(accessService.getAccessesByAirportId(1L)).thenReturn(Collections.singletonList(access));
        when(accessMapper.toDTO(any(Access.class))).thenReturn(accessDTO);

        mockMvc.perform(get("/access/airport/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void createAccess_shouldCreateAccess() throws Exception {
        Access access = new Access();
        access.setId(1L);
        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setId(1L);

        when(accessMapper.toEntity(any(AccessDTO.class))).thenReturn(access);
        when(accessService.addAccess(any(Access.class))).thenReturn(access);
        when(accessMapper.toDTO(any(Access.class))).thenReturn(accessDTO);

        mockMvc.perform(post("/access")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accessDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateAccessById_shouldUpdateAccess() throws Exception {
        Access access = new Access();
        access.setId(1L);
        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setId(1L);

        when(accessMapper.toEntity(any(AccessDTO.class))).thenReturn(access);
        when(accessService.updateAccess(any(Long.class), any(Access.class))).thenReturn(access);
        when(accessMapper.toDTO(any(Access.class))).thenReturn(accessDTO);

        mockMvc.perform(put("/access/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accessDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }



    @Test
    public void deleteAccessById_shouldDeleteAccess() throws Exception {
        mockMvc.perform(delete("/access/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getMyAccesses_shouldReturnMyAccesses() throws Exception {
        String keycloakId = "user-id";
        UserEntity user = new UserEntity();
        user.setId(1L);
        Access access = new Access();
        access.setId(1L);
        AccessDTO accessDTO = new AccessDTO();
        accessDTO.setId(1L);

        when(userEntityRepo.findByKeycloakId(keycloakId)).thenReturn(user);
        when(accessService.getAccessesByUserId(1L)).thenReturn(Collections.singletonList(access));
        when(accessMapper.toDTO(any(Access.class))).thenReturn(accessDTO);

        mockMvc.perform(get("/access/my")
                .with(jwt().jwt(builder -> builder.subject(keycloakId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
