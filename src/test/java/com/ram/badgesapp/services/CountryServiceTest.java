package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.repos.CountryRepo;
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
class CountryServiceTest {

    @Mock
    private CountryRepo countryRepo;

    @InjectMocks
    private CountryService countryService;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(1L);
        country.setName("Test Country");
    }

    @Test
    void getAllCountries() {
        when(countryRepo.findAll()).thenReturn(Collections.singletonList(country));
        List<Country> countries = countryService.getAllCountries();
        assertFalse(countries.isEmpty());
        assertEquals(1, countries.size());
    }

    @Test
    void getCountryById() {
        when(countryRepo.findById(1L)).thenReturn(Optional.of(country));
        Country foundCountry = countryService.getCountryById(1L);
        assertNotNull(foundCountry);
        assertEquals(1L, foundCountry.getId());
    }

    @Test
    void getCountryById_notFound() {
        when(countryRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            countryService.getCountryById(1L);
        });
    }

    @Test
    void createCountry() {
        when(countryRepo.save(any(Country.class))).thenReturn(country);
        Country savedCountry = countryService.createCountry(new Country());
        assertNotNull(savedCountry);
        assertEquals(1L, savedCountry.getId());
    }

    @Test
    void deleteCountry() {
        doNothing().when(countryRepo).deleteById(1L);
        countryService.deleteCountry(1L);
        verify(countryRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateCountry() {
        when(countryRepo.findById(1L)).thenReturn(Optional.of(country));
        when(countryRepo.save(any(Country.class))).thenReturn(country);

        Country updatedInfo = new Country();
        updatedInfo.setName("Updated Country");

        Country updatedCountry = countryService.updateCountry(1L, updatedInfo);

        assertNotNull(updatedCountry);
        assertEquals("Updated Country", updatedCountry.getName());
        verify(countryRepo).save(country);
    }

    @Test
    void updateCountry_notFound() {
        when(countryRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            countryService.updateCountry(1L, new Country());
        });
    }
}
