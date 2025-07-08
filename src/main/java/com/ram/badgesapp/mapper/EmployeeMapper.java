package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.EmployeeDTO;
import com.ram.badgesapp.entities.Employee;
import com.ram.badgesapp.mapper.helpers.EmployeeMapperHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { EmployeeMapperHelper.class})
public interface EmployeeMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "badgeId", source = "badge.id")
    EmployeeDTO toDTO(Employee employee);

    @Mapping(target = "company", source = "companyId", qualifiedByName = "companyFromId" )
    @Mapping(target = "badge", source = "badgeId", qualifiedByName = "badgeFromId")
    Employee toEntity(EmployeeDTO employeeDTO);

}
