package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.BadgeDTO;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.mapper.helpers.BadgeMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = { BadgeMapperHelper.class })
public interface BadgeMapper {

    @Mapping(target = "company", source = "companyId", qualifiedByName = "companyFromId")
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "employeeFromId")
    @Mapping(target = "accesList", ignore = true)
    Badge toEntity(BadgeDTO dto);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "accessListIds", expression = "java(badge.getAccesList().stream().map(access -> access.getId()).collect(java.util.stream.Collectors.toList()))")
    BadgeDTO toDTO(Badge badge);
}
