package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.Employee;
import com.ram.badgesapp.repos.CompanyRepo;
import com.ram.badgesapp.repos.EmployeeRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BadgeMapperHelper {
    @Autowired
    private CompanyRepo companyRepository;

    @Autowired
    private EmployeeRepo employeeRepository;

    @Named("companyFromId")
    public Company companyFromId(Long id) {
        if (id == null) return null;
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Named("employeeFromId")
    public Employee employeeFromId(Long id) {
        if (id == null) return null;
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }
}
