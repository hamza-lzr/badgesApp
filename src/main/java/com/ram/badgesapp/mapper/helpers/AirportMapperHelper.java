package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.repos.CityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AirportMapperHelper {

    @Autowired
    private CityRepo cityRepo;

    @Named("cityFromId")
    public City cityFromId(Long id) {
        if (id == null) return null;
        return cityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found with id: " + id));
    }

}
