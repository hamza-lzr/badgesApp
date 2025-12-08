package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.repos.AirportRepo;
import com.ram.badgesapp.repos.BadgeRepo;
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
class AccessMapperHelperTest {

    @Mock
    private BadgeRepo badgeRepo;

    @Mock
    private AirportRepo airportRepo;

    @InjectMocks
    private AccessMapperHelper accessMapperHelper;

    private Badge testBadge;
    private Airport testAirport;

    @BeforeEach
    void setUp() {
        testBadge = new Badge();
        testBadge.setId(1L);
        testBadge.setCode("Test Badge");

        testAirport = new Airport();
        testAirport.setId(1L);
        testAirport.setName("Test Airport");
    }

    @Test
    void badgeFromId_validId_returnsBadge() {
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(testBadge));
        Badge result = accessMapperHelper.badgeFromId(1L);
        assertNotNull(result);
        assertEquals(testBadge.getId(), result.getId());
        verify(badgeRepo, times(1)).findById(1L);
    }

    @Test
    void badgeFromId_nullId_returnsNull() {
        Badge result = accessMapperHelper.badgeFromId(null);
        assertNull(result);
        verify(badgeRepo, never()).findById(anyLong());
    }

    @Test
    void badgeFromId_badgeNotFound_throwsRuntimeException() {
        when(badgeRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accessMapperHelper.badgeFromId(99L));
        assertEquals("Badge not found with id: 99", exception.getMessage());
        verify(badgeRepo, times(1)).findById(99L);
    }

    @Test
    void airportFromId_validId_returnsAirport() {
        when(airportRepo.findById(1L)).thenReturn(Optional.of(testAirport));
        Airport result = accessMapperHelper.airportFromId(1L);
        assertNotNull(result);
        assertEquals(testAirport.getId(), result.getId());
        verify(airportRepo, times(1)).findById(1L);
    }

    @Test
    void airportFromId_nullId_returnsNull() {
        Airport result = accessMapperHelper.airportFromId(null);
        assertNull(result);
        verify(airportRepo, never()).findById(anyLong());
    }

    @Test
    void airportFromId_airportNotFound_throwsRuntimeException() {
        when(airportRepo.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accessMapperHelper.airportFromId(99L));
        assertEquals("Airport not found with id: 99", exception.getMessage());
        verify(airportRepo, times(1)).findById(99L);
    }
}
