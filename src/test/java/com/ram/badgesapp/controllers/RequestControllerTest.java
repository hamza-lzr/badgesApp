package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.RequestsDTO;
import com.ram.badgesapp.entities.ReqStatus;
import com.ram.badgesapp.entities.ReqType;
import com.ram.badgesapp.entities.Requests;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.RequestsMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.RequestsService;
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
@WebMvcTest(RequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestsService reqService;

    @MockBean
    private RequestsMapper reqMapper;

    @MockBean
    private UserEntityRepo userEntityRepo;

    @Test
    public void getAllRequests_shouldReturnListOfRequests() throws Exception {
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(reqService.getAllRequests()).thenReturn(Collections.singletonList(request));
        when(reqMapper.toDTO(any(Requests.class))).thenReturn(requestDTO);

        mockMvc.perform(get("/requests")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getRequestById_shouldReturnRequest() throws Exception {
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(reqService.getRequest(1L)).thenReturn(request);
        when(reqMapper.toDTO(request)).thenReturn(requestDTO);

        mockMvc.perform(get("/requests/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteRequest_shouldDeleteRequest() throws Exception {
        mockMvc.perform(delete("/requests/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void addRequest_shouldCreateRequest() throws Exception {
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(reqMapper.toEntity(any(RequestsDTO.class))).thenReturn(request);
        when(reqService.saveRequest(any(Requests.class))).thenReturn(request);
        when(reqMapper.toDTO(any(Requests.class))).thenReturn(requestDTO);

        mockMvc.perform(post("/requests")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateRequest_shouldUpdateRequest() throws Exception {
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(reqMapper.toEntity(any(RequestsDTO.class))).thenReturn(request);
        when(reqService.updateRequest(any(Long.class), any(Requests.class))).thenReturn(request);
        when(reqMapper.toDTO(any(Requests.class))).thenReturn(requestDTO);

        mockMvc.perform(put("/requests/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateRequestType_shouldUpdateRequestType() throws Exception {
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(reqService.updateReqType(any(Long.class), any(ReqType.class))).thenReturn(request);
        when(reqMapper.toDTO(any(Requests.class))).thenReturn(requestDTO);

        mockMvc.perform(put("/requests/update-type/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ReqType.NEW_BADGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateRequestStatus_shouldUpdateRequestStatus() throws Exception {
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(reqService.updateReqStatus(any(Long.class), any(ReqStatus.class))).thenReturn(request);
        when(reqMapper.toDTO(any(Requests.class))).thenReturn(requestDTO);

        mockMvc.perform(put("/requests/update-status/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ReqStatus.PENDING)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getMyRequests_shouldReturnMyRequests() throws Exception {
        String keycloakId = "user-id";
        UserEntity user = new UserEntity();
        user.setId(1L);
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(userEntityRepo.findByKeycloakId(keycloakId)).thenReturn(user);
        when(reqService.getRequestsByEmployeeId(1L)).thenReturn(Collections.singletonList(request));
        when(reqMapper.toDTO(any(Requests.class))).thenReturn(requestDTO);

        mockMvc.perform(get("/requests/my")
                .with(jwt().jwt(builder -> builder.subject(keycloakId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void createMyRequest_shouldCreateRequest() throws Exception {
        String keycloakId = "user-id";
        UserEntity user = new UserEntity();
        user.setId(1L);
        Requests request = new Requests();
        request.setId(1L);
        RequestsDTO requestDTO = new RequestsDTO();
        requestDTO.setId(1L);

        when(userEntityRepo.findByKeycloakId(keycloakId)).thenReturn(user);
        when(reqMapper.toEntity(any(RequestsDTO.class))).thenReturn(request);
        when(reqService.saveRequest(any(Requests.class))).thenReturn(request);
        when(reqMapper.toDTO(any(Requests.class))).thenReturn(requestDTO);

        mockMvc.perform(post("/requests/my")
                .with(jwt().jwt(builder -> builder.subject(keycloakId)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}
