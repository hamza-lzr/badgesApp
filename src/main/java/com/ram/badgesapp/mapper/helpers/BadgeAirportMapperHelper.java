package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.repos.AirportRepo;
import com.ram.badgesapp.repos.BadgeRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BadgeAirportMapperHelper {

    @Autowired
    private BadgeRepo badgeRepo;

    @Autowired
    private AirportRepo airportRepo;

    @Named("badgeFromId")
    public Badge badgeFromId(Long id) {
        if (id == null) return null;
        return badgeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge not found with id: " + id));
    }

    @Named("airportFromId")
    public Airport airportFromId(Long id) {
        if (id == null) return null;
        return airportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with id: " + id));
    }
}
