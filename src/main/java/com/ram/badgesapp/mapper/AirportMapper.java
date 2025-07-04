package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.AirportDTO;
import com.ram.badgesapp.entities.Airport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    AirportDTO toDTO(Airport airport);
    Airport toEntity(AirportDTO dto);

}
