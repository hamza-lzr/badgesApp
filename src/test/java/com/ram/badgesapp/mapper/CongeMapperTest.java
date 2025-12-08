package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CongeDTO;
import com.ram.badgesapp.entities.Conge;
import com.ram.badgesapp.entities.StatusConge;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class CongeMapperTest {

    @Autowired
    private CongeMapper congeMapper;

    @MockitoBean
    private UserEntityRepo userEntityRepo;

    private Conge conge;
    private CongeDTO congeDTO;
    private UserEntity user;

    private final LocalDate startDate = LocalDate.of(2023, 1, 10);
    private final LocalDate endDate = LocalDate.of(2023, 1, 20);
    private final LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 9, 0, 0);

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        // For toDTO test
        conge = new Conge();
        conge.setId(1L);
        conge.setUser(user);
        conge.setStartDate(startDate);
        conge.setEndDate(endDate);
        conge.setDescription("Vacation");
        conge.setStatus(StatusConge.APPROVED);
        conge.setCreatedAt(createdAt);

        // For toEntity test
        congeDTO = new CongeDTO();
        congeDTO.setId(1L);
        congeDTO.setUserId(1L);
        congeDTO.setStartDate(startDate.format(DateTimeFormatter.ISO_DATE));
        congeDTO.setEndDate(endDate.format(DateTimeFormatter.ISO_DATE));
        congeDTO.setDescription("Vacation DTO");
        congeDTO.setStatus(StatusConge.PENDING);
        congeDTO.setCreatedAt(createdAt.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Test
    void testToDTO() {
        CongeDTO resultDTO = congeMapper.toDTO(conge);

        assertNotNull(resultDTO);
        assertEquals(conge.getId(), resultDTO.getId());
        assertEquals(conge.getUser().getId(), resultDTO.getUserId());
        assertEquals(conge.getDescription(), resultDTO.getDescription());
        assertEquals(conge.getStatus(), resultDTO.getStatus());
        assertEquals("2023-01-10", resultDTO.getStartDate());
        assertEquals("2023-01-20", resultDTO.getEndDate());
        assertEquals("2023-01-01T09:00:00", resultDTO.getCreatedAt());
    }

    @Test
    void testToEntity() {
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));

        Conge resultEntity = congeMapper.toEntity(congeDTO);

        assertNotNull(resultEntity);
        assertEquals(congeDTO.getId(), resultEntity.getId());
        assertEquals(user, resultEntity.getUser());
        assertEquals(congeDTO.getDescription(), resultEntity.getDescription());
        assertEquals(congeDTO.getStatus(), resultEntity.getStatus());
        assertEquals(startDate, resultEntity.getStartDate());
        assertEquals(endDate, resultEntity.getEndDate());
        assertEquals(createdAt, resultEntity.getCreatedAt());
    }
}
