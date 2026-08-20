package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CompanyDTO;
import com.ram.badgesapp.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyDTO toDTO(Company company);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "badges", ignore = true)
    Company toEntity(CompanyDTO dto);

}
