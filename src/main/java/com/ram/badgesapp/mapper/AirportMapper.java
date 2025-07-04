package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.AirportDTO;
import com.ram.badgesapp.entities.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    @Mapping(target = "badgeAirports", ignore = true)
    Airport toEntity(AirportDTO dto);

    AirportDTO toDTO(Airport airport);

}
