package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.CongeDTO;
import com.ram.badgesapp.entities.Conge;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.CongeMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.CongeService;
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

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CongeController.class)
public class CongeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CongeService congeService;

    @MockitoBean
    private CongeMapper congeMapper;

    @MockitoBean
    private UserEntityRepo userEntityRepo;

    @Test
    public void getAllConges_shouldReturnListOfConges() throws Exception {
        Conge conge = new Conge();
        conge.setId(1L);
        CongeDTO congeDTO = new CongeDTO();
        congeDTO.setId(1L);

        when(congeService.getAllConges()).thenReturn(Collections.singletonList(conge));
        when(congeMapper.toDTO(any(Conge.class))).thenReturn(congeDTO);

        mockMvc.perform(get("/conge")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getCongeById_shouldReturnConge() throws Exception {
        Conge conge = new Conge();
        conge.setId(1L);
        CongeDTO congeDTO = new CongeDTO();
        congeDTO.setId(1L);

        when(congeService.getCongeById(1L)).thenReturn(Optional.of(conge));
        when(congeMapper.toDTO(conge)).thenReturn(congeDTO);

        mockMvc.perform(get("/conge/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void saveConge_shouldCreateConge() throws Exception {
        Conge conge = new Conge();
        conge.setId(1L);
        CongeDTO congeDTO = new CongeDTO();
        congeDTO.setId(1L);

        when(congeMapper.toEntity(any(CongeDTO.class))).thenReturn(conge);
        when(congeService.saveConge(any(Conge.class))).thenReturn(conge);
        when(congeMapper.toDTO(any(Conge.class))).thenReturn(congeDTO);

        mockMvc.perform(post("/conge")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(congeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteConge_shouldDeleteConge() throws Exception {
        mockMvc.perform(delete("/conge/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateConge_shouldUpdateConge() throws Exception {
        Conge conge = new Conge();
        conge.setId(1L);
        CongeDTO congeDTO = new CongeDTO();
        congeDTO.setId(1L);

        when(congeMapper.toEntity(any(CongeDTO.class))).thenReturn(conge);
        when(congeService.getCongeById(1L)).thenReturn(Optional.of(conge));
        when(congeMapper.toDTO(any(Conge.class))).thenReturn(congeDTO);


        mockMvc.perform(put("/conge/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(congeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateStatusToApproved_shouldUpdateStatus() throws Exception {
        Conge conge = new Conge();
        conge.setId(1L);
        CongeDTO congeDTO = new CongeDTO();
        congeDTO.setId(1L);

        when(congeService.getCongeById(1L)).thenReturn(Optional.of(conge));
        when(congeMapper.toDTO(any(Conge.class))).thenReturn(congeDTO);

        mockMvc.perform(put("/conge/approve/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateStatusToRejected_shouldUpdateStatus() throws Exception {
        Conge conge = new Conge();
        conge.setId(1L);
        CongeDTO congeDTO = new CongeDTO();
        congeDTO.setId(1L);

        when(congeService.getCongeById(1L)).thenReturn(Optional.of(conge));
        when(congeMapper.toDTO(any(Conge.class))).thenReturn(congeDTO);

        mockMvc.perform(put("/conge/reject/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getMyConges_shouldReturnMyConges() throws Exception {
        String keycloakId = "user-id";
        UserEntity user = new UserEntity();
        user.setId(1L);
        Conge conge = new Conge();
        conge.setId(1L);
        CongeDTO congeDTO = new CongeDTO();
        congeDTO.setId(1L);

        when(userEntityRepo.findByKeycloakId(keycloakId)).thenReturn(user);
        when(congeService.getCongesByUserId(1L)).thenReturn(Collections.singletonList(conge));
        when(congeMapper.toDTO(any(Conge.class))).thenReturn(congeDTO);

        mockMvc.perform(get("/conge/my")
                .with(jwt().jwt(builder -> builder.subject(keycloakId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
