package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.CongeDTO;
import com.ram.badgesapp.entities.Conge;
import com.ram.badgesapp.mapper.helpers.CongeMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = { CongeMapperHelper.class })
public interface CongeMapper {

    @Mappings({
        @Mapping(target = "userId", source = "user.id"),
        @Mapping(target = "startDate", source = "startDate", qualifiedByName = "formatLocalDate"),
        @Mapping(target = "endDate", source = "endDate", qualifiedByName = "formatLocalDate"),
        @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "formatLocalDateTime"),
        @Mapping(target = "status", source = "status")
    })
    CongeDTO toDTO(Conge conge);
    
    @Mappings({
        @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId"),
        @Mapping(target = "startDate", source = "startDate", qualifiedByName = "parseLocalDate"),
        @Mapping(target = "endDate", source = "endDate", qualifiedByName = "parseLocalDate"),
        @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "parseLocalDateTime"),
        @Mapping(target = "status", source = "status")
    })
    Conge toEntity(CongeDTO congeDTO);
}
