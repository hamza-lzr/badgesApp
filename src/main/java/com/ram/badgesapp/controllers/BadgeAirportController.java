package com.ram.badgesapp.controllers;


import com.ram.badgesapp.dto.BadgeAirportDTO;
import com.ram.badgesapp.entities.BadgeAirport;
import com.ram.badgesapp.mapper.BadgeAirportMapper;
import com.ram.badgesapp.services.BadgeAirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/badge-airport")
@CrossOrigin(origins = "*")
public class BadgeAirportController {

    private final BadgeAirportService badgeAirportService;
    private final BadgeAirportMapper badgeAirportMapper;
    public BadgeAirportController(BadgeAirportService badgeAirportService, BadgeAirportMapper badgeAirportMapper) {
        this.badgeAirportService = badgeAirportService;
        this.badgeAirportMapper = badgeAirportMapper;
    }

    @GetMapping
    public ResponseEntity<List<BadgeAirportDTO>> getAllAccesses() {
        return ResponseEntity.ok(badgeAirportService.getAllAccesses().stream()
                .map(badgeAirportMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeAirportDTO> getAccessById(@PathVariable Long id) {
        return ResponseEntity.ok(badgeAirportMapper.toDTO(badgeAirportService.getAccessById(id)));
    }

    @GetMapping("/badge/{id}")
    public ResponseEntity<List<BadgeAirportDTO>> getAccessesByBadgeId(@PathVariable Long id) {
        return ResponseEntity.ok(badgeAirportService.getAccessesByBadgeId(id).stream()
                .map(badgeAirportMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/airport/{id}")
    public ResponseEntity<List<BadgeAirportDTO>> getAccessesByAirportId(@PathVariable Long id) {
        return ResponseEntity.ok(badgeAirportService.getAccessesByAirportId(id).stream()
                .map(badgeAirportMapper::toDTO)
                .toList()
        );
    }

    @PostMapping
    public ResponseEntity<BadgeAirportDTO> createAccess(@RequestBody BadgeAirportDTO badgeAirportDTO) {
        BadgeAirport badgeAirport = badgeAirportMapper.toEntity(badgeAirportDTO);
        return ResponseEntity.ok(badgeAirportMapper.toDTO(badgeAirportService.addAccess(badgeAirport)));
    }

    //TO-DO

}
