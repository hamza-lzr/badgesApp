package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.repos.CityRepo;
import jakarta.persistence.EntityNotFoundException;
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
class AirportMapperHelperTest {

    @Mock
    private CityRepo cityRepo;

    @InjectMocks
    private AirportMapperHelper airportMapperHelper;

    private City testCity;

    @BeforeEach
    void setUp() {
        testCity = new City();
        testCity.setId(1L);
        testCity.setName("Test City");
    }

    @Test
    void cityFromId_validId_returnsCity() {
        when(cityRepo.findById(1L)).thenReturn(Optional.of(testCity));
        City result = airportMapperHelper.cityFromId(1L);
        assertNotNull(result);
        assertEquals(testCity.getId(), result.getId());
        verify(cityRepo, times(1)).findById(1L);
    }

    @Test
    void cityFromId_nullId_returnsNull() {
        City result = airportMapperHelper.cityFromId(null);
        assertNull(result);
        verify(cityRepo, never()).findById(anyLong());
    }

    @Test
    void cityFromId_cityNotFound_throwsEntityNotFoundException() {
        when(cityRepo.findById(anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> airportMapperHelper.cityFromId(99L));
        assertEquals("City not found with id: 99", exception.getMessage());
        verify(cityRepo, times(1)).findById(99L);
    }
}
