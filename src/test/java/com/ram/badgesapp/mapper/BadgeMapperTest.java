package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.BadgeDTO;
import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.CompanyRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BadgeMapperTest {

    @Autowired
    private BadgeMapper badgeMapper;

    @MockitoBean
    private CompanyRepo companyRepo;

    @MockitoBean
    private UserEntityRepo userEntityRepo;

    private Badge badge;
    private BadgeDTO badgeDTO;
    private Company company;
    private UserEntity user;
    private Access access1;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(10L);

        user = new UserEntity();
        user.setId(1L);

        access1 = new Access();
        access1.setId(101L);

        // For toDTO test
        badge = new Badge();
        badge.setId(1L);
        badge.setCode("SN123");
        badge.setCompany(company);
        badge.setUser(user);
        badge.setStatus(BadgeStatus.ACTIVE);
        badge.setAccesList(Arrays.asList(access1));


        // For toEntity test
        badgeDTO = new BadgeDTO();
        badgeDTO.setId(1L);
        badgeDTO.setCode("SN456");
        badgeDTO.setCompanyId(10L);
        badgeDTO.setUserId(1L);
        badgeDTO.setStatus(BadgeStatus.INACTIVE);
        // accessListIds is ignored in toEntity, so no need to set it
    }

    @Test
    void testToDTO() {
        // Handle case where accesList might be null
        badge.setAccesList(new ArrayList<>());
        BadgeDTO resultDtoWithEmptyAccess = badgeMapper.toDTO(badge);
        assertNotNull(resultDtoWithEmptyAccess.getAccessListIds());
        assertTrue(resultDtoWithEmptyAccess.getAccessListIds().isEmpty());

        // Test with non-empty list
        badge.setAccesList(Arrays.asList(access1));
        BadgeDTO resultDTO = badgeMapper.toDTO(badge);

        assertNotNull(resultDTO);
        assertEquals(badge.getId(), resultDTO.getId());
        assertEquals(badge.getCode(), resultDTO.getCode());
        assertEquals(badge.getCompany().getId(), resultDTO.getCompanyId());
        assertEquals(badge.getUser().getId(), resultDTO.getUserId());
        assertEquals(badge.getStatus(), resultDTO.getStatus());
        assertEquals(1, resultDTO.getAccessListIds().size());
        assertEquals(access1.getId(), resultDTO.getAccessListIds().get(0));
    }

    @Test
    void testToEntity() {
        when(companyRepo.findById(10L)).thenReturn(Optional.of(company));
        when(userEntityRepo.findById(1L)).thenReturn(Optional.of(user));

        Badge resultEntity = badgeMapper.toEntity(badgeDTO);

        assertNotNull(resultEntity);
        assertEquals(badgeDTO.getId(), resultEntity.getId());
        assertEquals(badgeDTO.getCode(), resultEntity.getCode());
        assertEquals(company, resultEntity.getCompany());
        assertEquals(user, resultEntity.getUser());
        assertEquals(badgeDTO.getStatus(), resultEntity.getStatus());
        assertEquals(0, resultEntity.getAccesList().size());
    }
}
