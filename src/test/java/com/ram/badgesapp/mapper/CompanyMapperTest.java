package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CompanyDTO;
import com.ram.badgesapp.entities.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CompanyMapperTest {

    @Autowired
    private CompanyMapper companyMapper;

    private Company company;
    private CompanyDTO companyDTO;

    @BeforeEach
    void setUp() {
        // For toDTO test
        company = new Company();
        company.setId(1L);
        company.setName("Test Company");
        company.setAddress("123 Test St");

        // For toEntity test
        companyDTO = new CompanyDTO();
        companyDTO.setId(2L);
        companyDTO.setName("Test Company DTO");
        companyDTO.setAddress("456 DTO Ave");
    }

    @Test
    void testToDTO() {
        CompanyDTO resultDTO = companyMapper.toDTO(company);

        assertNotNull(resultDTO);
        assertEquals(company.getId(), resultDTO.getId());
        assertEquals(company.getName(), resultDTO.getName());
        assertEquals(company.getAddress(), resultDTO.getAddress());
    }

    @Test
    void testToEntity() {
        Company resultEntity = companyMapper.toEntity(companyDTO);

        assertNotNull(resultEntity);
        assertEquals(companyDTO.getId(), resultEntity.getId());
        assertEquals(companyDTO.getName(), resultEntity.getName());
        assertEquals(companyDTO.getAddress(), resultEntity.getAddress());
    }
}
