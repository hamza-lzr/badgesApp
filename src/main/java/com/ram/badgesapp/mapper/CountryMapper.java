package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CountryDTO;
import com.ram.badgesapp.entities.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    // ➤ Entity → DTO
    @Mappings({
            @Mapping(target = "name", source = "name")
    })
    CountryDTO toDTO(Country country);

    // ➤ DTO → Entity
    @Mappings({
            @Mapping(target = "id", ignore = true),   // l'ID est géré par la base
            @Mapping(target = "cities", ignore = true) // on ne gère pas les villes ici
    })
    Country toEntity(CountryDTO countryDTO);
}

