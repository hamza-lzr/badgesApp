package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.UserDTO;
import com.ram.badgesapp.entities.*;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.CompanyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @MockitoBean
    private CompanyRepo companyRepo;

    @MockitoBean
    private BadgeRepo badgeRepo;

    private UserEntity userEntity;
    private UserDTO userDTO;
    private Company company;
    private Badge badge1;
    private Badge badge2;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(10L);
        company.setName("Test Corp");

        badge1 = new Badge();
        badge1.setId(1L);
        badge2 = new Badge();
        badge2.setId(2L);

        // Source for toDTO test
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");
        userEntity.setMatricule("M123");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setPhone("123456789");
        userEntity.setCompany(company);
        userEntity.setBadge(Arrays.asList(badge1, badge2));
        userEntity.setRole(Role.EMPLOYEE);
        userEntity.setStatus(Status.ACTIVE);

        // Source for toEntity test
        userDTO = new UserDTO();
        userDTO.setId(2L);
        userDTO.setMatricule("M123");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPhone("123456789");
        userDTO.setCompanyId(10L);
        userDTO.setBadgesIds(Arrays.asList(1L, 2L));
        userDTO.setRole("EMPLOYEE");
        userDTO.setStatus("ACTIVE");

    }

    @Test
    void testToDTO() {
        UserDTO resultDTO = userMapper.toDTO(userEntity);

        assertNotNull(resultDTO);
        assertEquals(userEntity.getId(), resultDTO.getId());
        assertEquals(userEntity.getEmail(), resultDTO.getEmail());
        assertEquals(userEntity.getMatricule(), resultDTO.getMatricule());
        assertEquals(userEntity.getFirstName(), resultDTO.getFirstName());
        assertEquals(userEntity.getLastName(), resultDTO.getLastName());
        assertEquals(userEntity.getPhone(), resultDTO.getPhone());
        assertEquals(userEntity.getCompany().getId(), resultDTO.getCompanyId());
        assertEquals(2, resultDTO.getBadgesIds().size());
        assertTrue(resultDTO.getBadgesIds().containsAll(Arrays.asList(1L, 2L)));
        assertEquals("EMPLOYEE", resultDTO.getRole());
        assertEquals("ACTIVE", resultDTO.getStatus());
    }

    @Test
    void testToEntity() {
        // Mock the helper calls
        when(companyRepo.findById(10L)).thenReturn(Optional.of(company));
        when(badgeRepo.findById(1L)).thenReturn(Optional.of(badge1));
        when(badgeRepo.findById(2L)).thenReturn(Optional.of(badge2));

        UserEntity resultEntity = userMapper.toEntity(userDTO);

        assertNotNull(resultEntity);
        // Ignored fields
        assertNotEquals(userDTO.getId(), resultEntity.getId()); // ID should be ignored
        assertNull(resultEntity.getPassword()); // Password should be ignored

        // Mapped fields
        assertEquals(userDTO.getEmail(), resultEntity.getEmail());
        assertEquals(userDTO.getMatricule(), resultEntity.getMatricule());
        assertEquals(userDTO.getFirstName(), resultEntity.getFirstName());
        assertEquals(userDTO.getLastName(), resultEntity.getLastName());
        assertEquals(userDTO.getPhone(), resultEntity.getPhone());
        assertEquals(company, resultEntity.getCompany());
        assertEquals(2, resultEntity.getBadge().size());
        assertTrue(resultEntity.getBadge().containsAll(Arrays.asList(badge1, badge2)));
        assertEquals(Role.EMPLOYEE, resultEntity.getRole());
        assertEquals(Status.ACTIVE, resultEntity.getStatus());
    }
}
