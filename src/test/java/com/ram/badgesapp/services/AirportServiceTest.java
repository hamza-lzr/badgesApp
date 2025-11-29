package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.repos.AirportRepo;
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
class AirportServiceTest {

    @Mock
    private AirportRepo airportRepo;

    @InjectMocks
    private AirportService airportService;

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport();
        airport.setId(1L);
        airport.setName("Test Airport");
        airport.setIata("TST");
    }

    @Test
    void getAllAirports() {
        when(airportRepo.findAll()).thenReturn(Collections.singletonList(airport));
        List<Airport> airports = airportService.getAllAirports();
        assertFalse(airports.isEmpty());
        assertEquals(1, airports.size());
    }

    @Test
    void getAirport() {
        when(airportRepo.findById(1L)).thenReturn(Optional.of(airport));
        Airport foundAirport = airportService.getAirport(1L);
        assertNotNull(foundAirport);
        assertEquals(1L, foundAirport.getId());
    }

    @Test
    void getAirport_notFound() {
        when(airportRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            airportService.getAirport(1L);
        });
    }

    @Test
    void createAirport() {
        when(airportRepo.save(any(Airport.class))).thenReturn(airport);
        Airport savedAirport = airportService.createAirport(new Airport());
        assertNotNull(savedAirport);
        assertEquals(1L, savedAirport.getId());
    }

    @Test
    void updateAirport() {
        when(airportRepo.findById(1L)).thenReturn(Optional.of(airport));
        when(airportRepo.save(any(Airport.class))).thenReturn(airport);

        Airport updatedInfo = new Airport();
        updatedInfo.setName("Updated Airport");

        Airport updatedAirport = airportService.updateAirport(1L, updatedInfo);

        assertNotNull(updatedAirport);
        assertEquals("Updated Airport", updatedAirport.getName());
        verify(airportRepo).save(airport);
    }

    @Test
    void updateAirport_notFound() {
        when(airportRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            airportService.updateAirport(1L, new Airport());
        });
    }

    @Test
    void deleteAirport() {
        doNothing().when(airportRepo).deleteById(1L);
        airportService.deleteAirport(1L);
        verify(airportRepo, times(1)).deleteById(1L);
    }
}
