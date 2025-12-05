package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.CountryDTO;
import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.mapper.CountryMapper;
import com.ram.badgesapp.services.CountryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CountryService countryService;

    @MockitoBean
    private CountryMapper countryMapper;

    @Test
    public void getAllCountries_shouldReturnListOfCountries() throws Exception {
        Country country = new Country();
        country.setId(1L);
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);

        when(countryService.getAllCountries()).thenReturn(Collections.singletonList(country));
        when(countryMapper.toDTO(any(Country.class))).thenReturn(countryDTO);

        mockMvc.perform(get("/country")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getCountryById_shouldReturnCountry() throws Exception {
        Country country = new Country();
        country.setId(1L);
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);

        when(countryService.getCountryById(1L)).thenReturn(country);
        when(countryMapper.toDTO(country)).thenReturn(countryDTO);

        mockMvc.perform(get("/country/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteCountryById_shouldDeleteCountry() throws Exception {
        mockMvc.perform(delete("/country/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void createCountry_shouldCreateCountry() throws Exception {
        Country country = new Country();
        country.setId(1L);
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);

        when(countryMapper.toEntity(any(CountryDTO.class))).thenReturn(country);
        when(countryService.createCountry(any(Country.class))).thenReturn(country);
        when(countryMapper.toDTO(any(Country.class))).thenReturn(countryDTO);

        mockMvc.perform(post("/country")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(countryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateCountryById_shouldUpdateCountry() throws Exception {
        Country country = new Country();
        country.setId(1L);
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);

        when(countryMapper.toEntity(any(CountryDTO.class))).thenReturn(country);
        when(countryService.updateCountry(any(Long.class), any(Country.class))).thenReturn(country);
        when(countryMapper.toDTO(any(Country.class))).thenReturn(countryDTO);

        mockMvc.perform(put("/country/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(countryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}
