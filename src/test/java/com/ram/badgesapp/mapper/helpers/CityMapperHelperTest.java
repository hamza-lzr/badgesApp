package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.repos.CountryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityMapperHelperTest {

    @Mock
    private CountryRepo countryRepo;

    @InjectMocks
    private CityMapperHelper cityMapperHelper;

    private Country testCountry;

    @BeforeEach
    void setUp() {
        testCountry = new Country();
        testCountry.setId(1L);
        testCountry.setName("Test Country");
    }

    @Test
    void countryFromId_validId_returnsCountry() {
        when(countryRepo.findById(1L)).thenReturn(Optional.of(testCountry));
        Country result = cityMapperHelper.countryFromId(1L);
        assertNotNull(result);
        assertEquals(testCountry.getId(), result.getId());
        verify(countryRepo, times(1)).findById(1L);
    }

    @Test
    void countryFromId_nullId_returnsNull() {
        Country result = cityMapperHelper.countryFromId(null);
        assertNull(result);
        verify(countryRepo, never()).findById(anyLong());
    }

    @Test
    void countryFromId_countryNotFound_throwsRuntimeException() {
        when(countryRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cityMapperHelper.countryFromId(99L));
        assertEquals("Country not found with id: 99", exception.getMessage());
        verify(countryRepo, times(1)).findById(99L);
    }
}
