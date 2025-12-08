package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.AccessDTO;
import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.repos.AirportRepo;
import com.ram.badgesapp.repos.BadgeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccessMapperTest {

    @Autowired
    private AccessMapper accessMapper;

    @MockitoBean
    private BadgeRepo badgeRepo;

    @MockitoBean
    private AirportRepo airportRepo;

    private Access access;
    private AccessDTO accessDTO;
    private Badge badge;
    private Airport airport;

    @BeforeEach
    void setUp() {
        badge = new Badge();
        badge.setId(1L);

        airport = new Airport();
        airport.setId(100L);

        // For toDTO test
        access = new Access();
        access.setId(1L);
        access.setBadge(badge);
        access.setAirport(airport);
        access.setStartDate(java.time.LocalDate.now().minusDays(10));
        access.setEndDate(java.time.LocalDate.now().plusDays(20));

        // For toEntity test
        accessDTO = new AccessDTO();
        accessDTO.setId(1L);
        accessDTO.setBadgeId(1L);
        accessDTO.setAirportId(100L);
        accessDTO.setStartDate(java.time.LocalDate.now().minusDays(10));
        accessDTO.setEndDate(java.time.LocalDate.now().plusDays(20));
    }

    @Test
    void testToDTO() {
        AccessDTO resultDTO = accessMapper.toDTO(access);

        assertNotNull(resultDTO);
        assertEquals(access.getId(), resultDTO.getId());
        assertEquals(access.getBadge().getId(), resultDTO.getBadgeId());
        assertEquals(access.getAirport().getId(), resultDTO.getAirportId());
        assertEquals(access.getStartDate(), resultDTO.getStartDate());
        assertEquals(access.getEndDate(), resultDTO.getEndDate());
    }

    @Test
    void testToEntity() {
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(badge));
        when(airportRepo.findById(100L)).thenReturn(Optional.of(airport));

        Access resultEntity = accessMapper.toEntity(accessDTO);

        assertNotNull(resultEntity);
        assertEquals(accessDTO.getId(), resultEntity.getId());
        assertEquals(badge, resultEntity.getBadge());
        assertEquals(airport, resultEntity.getAirport());
        assertEquals(accessDTO.getStartDate(), resultEntity.getStartDate());
        assertEquals(accessDTO.getEndDate(), resultEntity.getEndDate());
    }
}
