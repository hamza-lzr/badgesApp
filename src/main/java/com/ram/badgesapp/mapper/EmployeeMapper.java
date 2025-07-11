package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.EmployeeDTO;
import com.ram.badgesapp.entities.Employee;
import com.ram.badgesapp.mapper.helpers.EmployeeMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployeeMapperHelper.class})
public interface EmployeeMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "badgeId", source = "badge.id")

    // Champs hérités
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "password", source = "password")  // facultatif, à sécuriser côté frontend si besoin

    EmployeeDTO toDTO(Employee employee);

    @Mapping(target = "company", source = "companyId", qualifiedByName = "companyFromId")
    @Mapping(target = "badge", source = "badgeId", qualifiedByName = "badgeFromId")

    @Mapping(target = "id", ignore = true) // la base gère l'id
    @Mapping(target = "password", ignore = true) // ou à générer dans le service

    // Champs hérités
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")

    Employee toEntity(EmployeeDTO employeeDTO);
}

