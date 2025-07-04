package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.BadgeDTO;
import com.ram.badgesapp.entities.Badge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BadgeMapper {

    @Mapping(target = "company", source = "companyId")
    @Mapping(target = "employee", source = "employeeId")
    @Mapping(target = "accesList", expression = "java(mapAccessListIdsToEntities(dto.getAccessListIds()))")
    Badge toEntity(BadgeDTO dto);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "accessListIds", expression = "java(mapAccessListToIds(badge.getAccesList()))")
    BadgeDTO toDTO(Badge badge);


}
