package com.ram.badgesapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ram.badgesapp.dto.CompanyDTO;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.mapper.CompanyMapper;
import com.ram.badgesapp.services.CompanyService;
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
@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompanyService companyService;

    @MockitoBean
    private CompanyMapper companyMapper;

    @Test
    public void getAllCompanies_shouldReturnListOfCompanies() throws Exception {
        Company company = new Company();
        company.setId(1L);
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(1L);

        when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
        when(companyMapper.toDTO(any(Company.class))).thenReturn(companyDTO);

        mockMvc.perform(get("/company")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void createCompany_shouldCreateCompany() throws Exception {
        Company company = new Company();
        company.setId(1L);
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(1L);

        when(companyMapper.toEntity(any(CompanyDTO.class))).thenReturn(company);
        when(companyService.createCompany(any(Company.class))).thenReturn(company);
        when(companyMapper.toDTO(any(Company.class))).thenReturn(companyDTO);

        mockMvc.perform(post("/company")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getCompanyById_shouldReturnCompany() throws Exception {
        Company company = new Company();
        company.setId(1L);
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(1L);

        when(companyService.getCompanyById(1L)).thenReturn(company);
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        mockMvc.perform(get("/company/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteCompanyById_shouldDeleteCompany() throws Exception {
        mockMvc.perform(delete("/company/1")
                .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCompanyById_shouldUpdateCompany() throws Exception {
        Company company = new Company();
        company.setId(1L);
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(1L);

        when(companyMapper.toEntity(any(CompanyDTO.class))).thenReturn(company);
        when(companyService.updateCompany(any(Long.class), any(Company.class))).thenReturn(company);
        when(companyMapper.toDTO(any(Company.class))).thenReturn(companyDTO);

        mockMvc.perform(put("/company/1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}
