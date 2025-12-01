package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.AirportDTO;
import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.mapper.AirportMapper;
import com.ram.badgesapp.services.AirportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
@WebMvcTest(AirportController.class)
public class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AirportService airportService;

    @MockBean
    private AirportMapper airportMapper;

    @Test
    public void getAllAirports_shouldReturnListOfAirports() throws Exception {
        Airport airport = new Airport();
        airport.setId(1L);
        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setId(1L);

        when(airportService.getAllAirports()).thenReturn(Collections.singletonList(airport));
        when(airportMapper.toDTO(any(Airport.class))).thenReturn(airportDTO);

        mockMvc.perform(get("/airport")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getAirportById_shouldReturnAirport() throws Exception {
        Airport airport = new Airport();
        airport.setId(1L);
        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setId(1L);

        when(airportService.getAirport(1L)).thenReturn(airport);
        when(airportMapper.toDTO(airport)).thenReturn(airportDTO);

        mockMvc.perform(get("/airport/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void createAirport_shouldCreateAirport() throws Exception {
        Airport airport = new Airport();
        airport.setId(1L);
        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setId(1L);

        when(airportMapper.toEntity(any(AirportDTO.class))).thenReturn(airport);
        when(airportService.createAirport(any(Airport.class))).thenReturn(airport);
        when(airportMapper.toDTO(any(Airport.class))).thenReturn(airportDTO);

        mockMvc.perform(post("/airport")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateAirportById_shouldUpdateAirport() throws Exception {
        Airport airport = new Airport();
        airport.setId(1L);
        AirportDTO airportDTO = new AirportDTO();
        airportDTO.setId(1L);

        when(airportMapper.toEntity(any(AirportDTO.class))).thenReturn(airport);
        when(airportService.updateAirport(any(Long.class), any(Airport.class))).thenReturn(airport);
        when(airportMapper.toDTO(any(Airport.class))).thenReturn(airportDTO);

        mockMvc.perform(put("/airport/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteAirportById_shouldDeleteAirport() throws Exception {
        mockMvc.perform(delete("/airport/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }
}
