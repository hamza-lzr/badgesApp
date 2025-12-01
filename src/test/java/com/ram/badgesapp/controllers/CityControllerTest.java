package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.CityDTO;
import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.mapper.CityMapper;
import com.ram.badgesapp.services.CityService;
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
@WebMvcTest(CityController.class)
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CityService cityService;

    @MockBean
    private CityMapper cityMapper;

    @Test
    public void getAllCities_shouldReturnListOfCities() throws Exception {
        City city = new City();
        city.setId(1L);
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(1L);

        when(cityService.getAllCities()).thenReturn(Collections.singletonList(city));
        when(cityMapper.toDTO(any(City.class))).thenReturn(cityDTO);

        mockMvc.perform(get("/city")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getCityById_shouldReturnCity() throws Exception {
        City city = new City();
        city.setId(1L);
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(1L);

        when(cityService.getCityById(1L)).thenReturn(city);
        when(cityMapper.toDTO(city)).thenReturn(cityDTO);

        mockMvc.perform(get("/city/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteCityById_shouldDeleteCity() throws Exception {
        mockMvc.perform(delete("/city/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void createCity_shouldCreateCity() throws Exception {
        City city = new City();
        city.setId(1L);
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(1L);

        when(cityMapper.toEntity(any(CityDTO.class))).thenReturn(city);
        when(cityService.createCity(any(City.class))).thenReturn(city);
        when(cityMapper.toDTO(any(City.class))).thenReturn(cityDTO);

        mockMvc.perform(post("/city")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateCityById_shouldUpdateCity() throws Exception {
        City city = new City();
        city.setId(1L);
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(1L);

        when(cityMapper.toEntity(any(CityDTO.class))).thenReturn(city);
        when(cityService.updateCity(any(Long.class), any(City.class))).thenReturn(city);
        when(cityMapper.toDTO(any(City.class))).thenReturn(cityDTO);

        mockMvc.perform(put("/city/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getCitiesByCountry_shouldReturnListOfCities() throws Exception {
        City city = new City();
        city.setId(1L);
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(1L);

        when(cityService.getCitiesByCountry(1L)).thenReturn(Collections.singletonList(city));
        when(cityMapper.toDTO(any(City.class))).thenReturn(cityDTO);

        mockMvc.perform(get("/city/country-id/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
