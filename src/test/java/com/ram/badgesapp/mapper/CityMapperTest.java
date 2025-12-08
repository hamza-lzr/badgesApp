package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CityDTO;
import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.repos.CountryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CityMapperTest {

    @Autowired
    private CityMapper cityMapper;

    @MockBean
    private CountryRepo countryRepo;

    private City city;
    private CityDTO cityDTO;
    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(1L);
        country.setName("Test Country");

        // For toDTO test
        city = new City();
        city.setId(10L);
        city.setName("Test City");
        city.setCountry(country);

        // For toEntity test
        cityDTO = new CityDTO();
        cityDTO.setName("Test City DTO");
        cityDTO.setCountryId(1L);
    }

    @Test
    void testToDTO() {
        CityDTO resultDTO = cityMapper.toDTO(city);

        assertNotNull(resultDTO);
        assertEquals(city.getId(), resultDTO.getId());
        assertEquals(city.getName(), resultDTO.getName());
        assertEquals(city.getCountry().getId(), resultDTO.getCountryId());
    }

    @Test
    void testToEntity() {
        when(countryRepo.findById(1L)).thenReturn(Optional.of(country));

        City resultEntity = cityMapper.toEntity(cityDTO);

        assertNotNull(resultEntity);
        assertNull(resultEntity.getId()); // ID should be ignored (defaults to 0 for long)
        assertEquals(cityDTO.getName(), resultEntity.getName());
        assertEquals(country, resultEntity.getCountry());
    }
}
