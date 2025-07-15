package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.AirportDTO;
import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.mapper.helpers.AirportMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AirportMapperHelper.class})
public interface AirportMapper {

    @Mapping(target = "city", source = "cityId", qualifiedByName = "cityFromId")
    @Mapping(target = "accesses", ignore = true)
    Airport toEntity(AirportDTO dto);

    @Mapping(target = "cityId", source = "city.id")
    AirportDTO toDTO(Airport airport);

}
