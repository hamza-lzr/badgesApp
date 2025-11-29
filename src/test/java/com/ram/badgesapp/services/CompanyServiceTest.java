package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.repos.CompanyRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepo companyRepo;

    @InjectMocks
    private CompanyService companyService;

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Test Company");
    }

    @Test
    void getAllCompanies() {
        when(companyRepo.findAll()).thenReturn(Collections.singletonList(company));
        List<Company> companies = companyService.getAllCompanies();
        assertFalse(companies.isEmpty());
        assertEquals(1, companies.size());
    }

    @Test
    void getCompanyById() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(company));
        Company foundCompany = companyService.getCompanyById(1L);
        assertNotNull(foundCompany);
        assertEquals(1L, foundCompany.getId());
    }

    @Test
    void getCompanyById_notFound() {
        when(companyRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            companyService.getCompanyById(1L);
        });
    }

    @Test
    void createCompany() {
        when(companyRepo.save(any(Company.class))).thenReturn(company);
        Company savedCompany = companyService.createCompany(new Company());
        assertNotNull(savedCompany);
        assertEquals(1L, savedCompany.getId());
    }

    @Test
    void deleteCompany() {
        doNothing().when(companyRepo).deleteById(1L);
        companyService.deleteCompany(1L);
        verify(companyRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateCompany() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepo.save(any(Company.class))).thenReturn(company);

        Company updatedInfo = new Company();
        updatedInfo.setName("Updated Company");

        Company updatedCompany = companyService.updateCompany(1L, updatedInfo);

        assertNotNull(updatedCompany);
        assertEquals("Updated Company", updatedCompany.getName());
        verify(companyRepo).save(company);
    }

    @Test
    void updateCompany_notFound() {
        when(companyRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            companyService.updateCompany(1L, new Company());
        });
    }
}
