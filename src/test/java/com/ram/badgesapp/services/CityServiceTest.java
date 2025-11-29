package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.repos.CityRepo;
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
class CityServiceTest {

    @Mock
    private CityRepo cityRepo;

    @InjectMocks
    private CityService cityService;

    private City city;
    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(1L);
        country.setName("Test Country");

        city = new City();
        city.setId(1L);
        city.setName("Test City");
        city.setCountry(country);
    }

    @Test
    void getAllCities() {
        when(cityRepo.findAll()).thenReturn(Collections.singletonList(city));
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty());
        assertEquals(1, cities.size());
    }

    @Test
    void getCityById() {
        when(cityRepo.findById(1L)).thenReturn(Optional.of(city));
        City foundCity = cityService.getCityById(1L);
        assertNotNull(foundCity);
        assertEquals(1L, foundCity.getId());
    }

    @Test
    void getCityById_notFound() {
        when(cityRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            cityService.getCityById(1L);
        });
    }

    @Test
    void createCity() {
        when(cityRepo.save(any(City.class))).thenReturn(city);
        City savedCity = cityService.createCity(new City());
        assertNotNull(savedCity);
        assertEquals(1L, savedCity.getId());
    }

    @Test
    void deleteCity() {
        doNothing().when(cityRepo).deleteById(1L);
        cityService.deleteCity(1L);
        verify(cityRepo, times(1)).deleteById(1L);
    }

    @Test
    void updateCity() {
        when(cityRepo.findById(1L)).thenReturn(Optional.of(city));
        when(cityRepo.save(any(City.class))).thenReturn(city);

        City updatedInfo = new City();
        updatedInfo.setName("Updated City");

        City updatedCity = cityService.updateCity(1L, updatedInfo);

        assertNotNull(updatedCity);
        assertEquals("Updated City", updatedCity.getName());
        verify(cityRepo).save(city);
    }

    @Test
    void updateCity_notFound() {
        when(cityRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            cityService.updateCity(1L, new City());
        });
    }

    @Test
    void getCitiesByCountry() {
        when(cityRepo.findAllByCountry_Id(1L)).thenReturn(Collections.singletonList(city));
        List<City> cities = cityService.getCitiesByCountry(1L);
        assertFalse(cities.isEmpty());
        assertEquals(1, cities.size());
    }
}
