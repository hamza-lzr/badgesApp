package com.ram.badgesapp.services;

import com.ram.badgesapp.dto.AirportDTO;
import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.repos.AirportRepo;
import com.ram.badgesapp.repos.CompanyRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

}
