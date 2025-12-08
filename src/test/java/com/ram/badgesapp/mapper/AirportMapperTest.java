package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.AirportDTO;
import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.repos.CityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AirportMapperTest {

    @Autowired
    private AirportMapper airportMapper;

    @MockBean
    private CityRepo cityRepo;

    private Airport airport;
    private AirportDTO airportDTO;
    private City city;

    @BeforeEach
    void setUp() {
        city = new City();
        city.setId(1L);
        city.setName("Test City");

        // For toDTO test
        airport = new Airport();
        airport.setId(100L);
        airport.setName("Test Airport");
        airport.setCity(city);

        // For toEntity test
        airportDTO = new AirportDTO();
        airportDTO.setId(100L);
        airportDTO.setName("Test Airport DTO");
        airportDTO.setCityId(1L);
    }

    @Test
    void testToDTO() {
        AirportDTO resultDTO = airportMapper.toDTO(airport);

        assertNotNull(resultDTO);
        assertEquals(airport.getId(), resultDTO.getId());
        assertEquals(airport.getName(), resultDTO.getName());
        assertEquals(airport.getCity().getId(), resultDTO.getCityId());
    }

    @Test
    void testToEntity() {
        when(cityRepo.findById(1L)).thenReturn(Optional.of(city));

        Airport resultEntity = airportMapper.toEntity(airportDTO);

        assertNotNull(resultEntity);
        assertEquals(airportDTO.getId(), resultEntity.getId());
        assertEquals(airportDTO.getName(), resultEntity.getName());
        assertEquals(city, resultEntity.getCity());
        assertEquals(0 ,resultEntity.getAccesses().size()); // Should be ignored
    }
}
