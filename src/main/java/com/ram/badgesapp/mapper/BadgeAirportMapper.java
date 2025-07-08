package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.BadgeAirportDTO;
import com.ram.badgesapp.entities.BadgeAirport;
import com.ram.badgesapp.mapper.helpers.BadgeAirportMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BadgeAirportMapperHelper.class})
public interface BadgeAirportMapper {

    @Mapping(target = "badge", source = "badgeId", qualifiedByName = "badgeFromId")
    @Mapping(target = "airport", source = "airportId", qualifiedByName = "airportFromId")
    BadgeAirport toEntity(BadgeAirportDTO dto);

    @Mapping(target = "badgeId", source = "badge.id")
    @Mapping(target = "airportId", source = "airport.id")
    BadgeAirportDTO toDTO(BadgeAirport badgeAirport);

}
