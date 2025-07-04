package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CompanyDTO;
import com.ram.badgesapp.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyDTO toDTO(Company company);

    Company toEntity(CompanyDTO dto);

}
