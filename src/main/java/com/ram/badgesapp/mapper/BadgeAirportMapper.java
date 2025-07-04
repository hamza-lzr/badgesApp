package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.BadgeAirportDTO;
import com.ram.badgesapp.entities.BadgeAirport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BadgeAirportMapper {

    @Mapping(target = "badge", source = "badgeId")
    @Mapping(target = "airport", source = "airportId")
    BadgeAirport toEntity(BadgeAirportDTO dto);

    @Mapping(target = "badgeId", source = "badge.id")
    @Mapping(target = "airportId", source = "airport.id")
    BadgeAirportDTO toDTO(BadgeAirport badgeAirport);
}
