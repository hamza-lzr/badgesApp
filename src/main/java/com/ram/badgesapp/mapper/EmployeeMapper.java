package com.ram.badgesapp.mapper;

import com.ram.badgesapp.dto.EmployeeDTO;
import com.ram.badgesapp.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "badgeId", source = "badge.id")
    @Mapping(target = "userId", source = "user.id")
    EmployeeDTO toDTO(Employee employee);

    @Mapping(target = "company", source = "companyId")
    @Mapping(target = "badge", source = "badgeId")
    @Mapping(target = "user", source = "userId")
    Employee toEntity(EmployeeDTO employeeDTO);

}
