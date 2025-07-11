package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.CompanyRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BadgeMapperHelper {
    @Autowired
    private CompanyRepo companyRepository;

    @Autowired
    private UserEntityRepo userEntityRepository;

    @Named("companyFromId")
    public Company companyFromId(Long id) {
        if (id == null) return null;
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Named("userFromId")
    public UserEntity userFromId(Long id) {
        if (id == null) return null;
        return userEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
