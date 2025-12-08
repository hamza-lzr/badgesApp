package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CountryDTO;
import com.ram.badgesapp.entities.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CountryMapperTest {

    @Autowired
    private CountryMapper countryMapper;

    private Country country;
    private CountryDTO countryDTO;

    @BeforeEach
    void setUp() {
        // For toDTO test
        country = new Country();
        country.setId(1L);
        country.setName("Test Country");

        // For toEntity test
        countryDTO = new CountryDTO();
        countryDTO.setId(2L); // This should be ignored
        countryDTO.setName("Test Country DTO");
    }

    @Test
    void testToDTO() {
        CountryDTO resultDTO = countryMapper.toDTO(country);

        assertNotNull(resultDTO);
        assertEquals(country.getId(), resultDTO.getId());
        assertEquals(country.getName(), resultDTO.getName());
    }

    @Test
    void testToEntity() {
        Country resultEntity = countryMapper.toEntity(countryDTO);

        assertNotNull(resultEntity);
        assertNull(resultEntity.getId()); // ID should be ignored
        assertNull(resultEntity.getCities()); // Cities should be ignored
        assertEquals(countryDTO.getName(), resultEntity.getName());
    }
}
