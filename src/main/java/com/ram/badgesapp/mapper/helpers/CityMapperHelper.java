package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.repos.CountryRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityMapperHelper {

    @Autowired
    private CountryRepo countryRepo;

    @Named("countryFromId")
    public Country countryFromId(Long id) {
        if (id == null) return null;
        return countryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
    }
}

