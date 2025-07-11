package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.UserDTO;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.helpers.UserMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserMapperHelper.class})
public interface UserMapper {

    // ➤ Entity -> DTO
    @Mappings({
            @Mapping(target = "companyId", source = "company.id"),
            @Mapping(target = "badgesIds", expression = "java(user.getBadge() != null ? user.getBadge().stream().map(b -> b.getId()).toList() : null)"),
            @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)"),
            @Mapping(target = "status", expression = "java(user.getStatus() != null ? user.getStatus().name() : null)"),

            @Mapping(target = "id", source = "id"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "matricule", source = "matricule"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "phone", source = "phone")
    })
    UserDTO toDTO(UserEntity user);


    // ➤ DTO -> Entity
    @Mappings({
            @Mapping(target = "company", source = "companyId", qualifiedByName = "companyFromId"),
            @Mapping(target = "badge", source = "badgesIds", qualifiedByName = "badgeListFromIds"),
            @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole"),
            @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus"),

            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", ignore = true),

            @Mapping(target = "email", source = "email"),
            @Mapping(target = "matricule", source = "matricule"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "phone", source = "phone")
    })
    UserEntity toEntity(UserDTO userDTO);
}
