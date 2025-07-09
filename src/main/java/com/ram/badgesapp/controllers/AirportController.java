package com.ram.badgesapp.controllers;


import com.ram.badgesapp.dto.AirportDTO;
import com.ram.badgesapp.entities.Airport;
import com.ram.badgesapp.mapper.AirportMapper;
import com.ram.badgesapp.services.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airport")
@CrossOrigin(origins = "*")
public class AirportController {

    private final AirportService airportService;
    private final AirportMapper airportMapper;
    public AirportController(AirportService airportService, AirportMapper airportMapper) {
        this.airportService = airportService;
        this.airportMapper = airportMapper;
    }

    @GetMapping
    public ResponseEntity<List<AirportDTO>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports().stream()
                .map(airportMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportDTO> getAirportById(@PathVariable Long id) {
        return ResponseEntity.ok(airportMapper.toDTO(airportService.getAirport(id)));
    }

    @PostMapping
    public ResponseEntity<AirportDTO> createAirport(@RequestBody AirportDTO airportDTO) {
        Airport airport = airportMapper.toEntity(airportDTO);
        Airport saved = airportService.createAirport(airport);
        return ResponseEntity.ok(airportMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportDTO> updateAirportById(@PathVariable Long id, @RequestBody AirportDTO airportDTO) {
        Airport airport = airportMapper.toEntity(airportDTO);
        Airport updated = airportService.updateAirport(id,airport);
        return ResponseEntity.ok(airportMapper.toDTO(updated));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAirportById(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.ok("Airport deleted successfully with id: " + id);
    }
}
