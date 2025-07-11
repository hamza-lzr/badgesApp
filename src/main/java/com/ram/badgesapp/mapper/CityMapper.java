package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CityDTO;
import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.mapper.helpers.CityMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CityMapperHelper.class})
public interface CityMapper {

    @Mappings({
            @Mapping(target = "countryId", source = "country.id")
    })
    CityDTO toDTO(City city);

    // ➤ DTO → Entity
    @Mappings({
            @Mapping(target = "country", source = "countryId", qualifiedByName = "countryFromId"),
            @Mapping(target = "id", ignore = true) // La base gère l'id
    })
    City toEntity(CityDTO cityDTO);
}

