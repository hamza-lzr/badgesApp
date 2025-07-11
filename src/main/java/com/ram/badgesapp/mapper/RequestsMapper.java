package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.RequestsDTO;
import com.ram.badgesapp.entities.Requests;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestsMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)         // géré par la BDD
    @Mapping(target = "createdAt", ignore = true)  // géré par le service ou la BDD
    Requests toEntity(RequestsDTO dto);

    @Mapping(source = "user.id", target = "userId")
    RequestsDTO toDTO(Requests entity);


}
