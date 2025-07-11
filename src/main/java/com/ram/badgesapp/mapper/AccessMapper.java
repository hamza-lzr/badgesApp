package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.AccessDTO;
import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.mapper.helpers.AccessMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AccessMapperHelper.class})
public interface AccessMapper {

    @Mapping(target = "badge", source = "badgeId", qualifiedByName = "badgeFromId")
    @Mapping(target = "airport", source = "airportId", qualifiedByName = "airportFromId")
    Access toEntity(AccessDTO dto);

    @Mapping(target = "badgeId", source = "badge.id")
    @Mapping(target = "airportId", source = "airport.id")
    AccessDTO toDTO(Access access);

}
