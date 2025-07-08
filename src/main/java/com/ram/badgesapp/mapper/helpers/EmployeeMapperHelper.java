package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.CompanyRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperHelper {

    @Autowired
    private BadgeRepo badgeRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Named("companyFromId")
    public Company companyFromId(Long id) {
        if (id == null) return null;
        return companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Named("badgeFromId")
    public com.ram.badgesapp.entities.Badge badgeFromId(Long id) {
        if (id == null) return null;
        return badgeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge not found with id: " + id));
    }

}
