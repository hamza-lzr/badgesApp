package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.BadgeDTO;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.BadgeMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.BadgeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
@WebMvcTest(BadgeController.class)
class BadgeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BadgeService badgeService;

    @MockitoBean
    private BadgeMapper badgeMapper;

    @MockitoBean
    private UserEntityRepo userEntityRepo;

    @Test
    public void getAllBadges_shouldReturnListOfBadges() throws Exception {
        Badge badge = new Badge();
        badge.setId(1L);
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setId(1L);

        when(badgeService.getAllBadges()).thenReturn(Collections.singletonList(badge));
        when(badgeMapper.toDTO(any(Badge.class))).thenReturn(badgeDTO);

        mockMvc.perform(get("/badges")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getBadgeById_asAdmin_shouldReturnBadge() throws Exception {
        Badge badge = new Badge();
        badge.setId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        badge.setUser(user);
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setId(1L);

        when(badgeService.getBadgeById(1L)).thenReturn(badge);
        when(badgeMapper.toDTO(badge)).thenReturn(badgeDTO);

        mockMvc.perform(get("/badges/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder.claim("realm_access", java.util.Map.of("roles", java.util.List.of("ADMIN"))))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }


    @Test
    public void deleteBadgeById_shouldDeleteBadge() throws Exception {
        mockMvc.perform(delete("/badges/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void createBadge_shouldCreateBadge() throws Exception {
        Badge badge = new Badge();
        badge.setId(1L);
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setId(1L);

        when(badgeMapper.toEntity(any(BadgeDTO.class))).thenReturn(badge);
        when(badgeService.createBadge(any(Badge.class))).thenReturn(badge);
        when(badgeMapper.toDTO(any(Badge.class))).thenReturn(badgeDTO);

        mockMvc.perform(post("/badges")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badgeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateBadgeById_shouldUpdateBadge() throws Exception {
        Badge badge = new Badge();
        badge.setId(1L);
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setId(1L);

        when(badgeMapper.toEntity(any(BadgeDTO.class))).thenReturn(badge);
        when(badgeService.updateBadge(any(Long.class), any(Badge.class))).thenReturn(badge);
        when(badgeMapper.toDTO(any(Badge.class))).thenReturn(badgeDTO);

        mockMvc.perform(put("/badges/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badgeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateExpiryDateById_shouldUpdateBadge() throws Exception {
        Badge badge = new Badge();
        badge.setId(1L);
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setId(1L);

        when(badgeMapper.toEntity(any(BadgeDTO.class))).thenReturn(badge);
        when(badgeService.updateExpiryDate(any(Long.class), any(Badge.class))).thenReturn(badge);
        when(badgeMapper.toDTO(any(Badge.class))).thenReturn(badgeDTO);

        mockMvc.perform(put("/badges/1/expiryDate")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badgeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }


    @Test
    public void getMyBadges_shouldReturnMyBadges() throws Exception {
        String keycloakId = "user-id";
        UserEntity user = new UserEntity();
        user.setId(1L);
        Badge badge = new Badge();
        badge.setId(1L);
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setId(1L);

        when(userEntityRepo.findByKeycloakId(keycloakId)).thenReturn(user);
        when(badgeService.getBadgesByUserId(1L)).thenReturn(Collections.singletonList(badge));
        when(badgeMapper.toDTO(any(Badge.class))).thenReturn(badgeDTO);

        mockMvc.perform(get("/badges/my")
                .with(jwt().jwt(builder -> builder.subject(keycloakId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
