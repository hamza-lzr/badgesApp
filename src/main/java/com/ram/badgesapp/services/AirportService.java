package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.repos.AirportRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportRepo airportRepo;
    public AirportService(AirportRepo airportRepo) {
        this.airportRepo = airportRepo;
    }

    public List<Airport> getAllAirports() {
        return airportRepo.findAll();
    }

    public Airport getAirport(Long id) {
        return airportRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airport with id " + id + " not found!"));
    }

    public Airport createAirport(Airport airport) {
        return airportRepo.save(airport);
    }

    public Airport updateAirport(Long id, Airport airport){
        Airport air = airportRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airport with id " + id + " not found!"));
        if (airport.getName() != null) {
            air.setName(airport.getName());
        }
        if (airport.getCity() != null) {
            air.setCity(airport.getCity());
        }


        if(airport.getIata() != null){
            air.setIata(airport.getIata());

        }
        airportRepo.save(air);
        return air;
    }

    public void deleteAirport(Long id){
        airportRepo.deleteById(id);
    }
}
