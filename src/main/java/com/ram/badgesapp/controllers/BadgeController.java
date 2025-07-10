package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.BadgeDTO;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.mapper.BadgeMapper;
import com.ram.badgesapp.services.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/badges")
@CrossOrigin(origins = "*")
public class BadgeController {

    private final BadgeService badgeService;
    private final BadgeMapper badgeMapper;
    public BadgeController(BadgeService badgeService, BadgeMapper badgeMapper) {
        this.badgeService = badgeService;
        this.badgeMapper = badgeMapper;
    }

    @GetMapping
    public ResponseEntity<List<BadgeDTO>> getAllBadges() {
        return ResponseEntity.ok(badgeService.getAllBadges().stream()
                .map(badgeMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeDTO> getBadgeById(@PathVariable Long id) {
        return ResponseEntity.ok(badgeMapper.toDTO(badgeService.getBadgeById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBadgeById(@PathVariable Long id) {
        badgeService.deleteBadge(id);
        return ResponseEntity.ok("Badge deleted successfully with id: " + id);
    }

    @PostMapping
    public ResponseEntity<BadgeDTO> createBadge(@RequestBody BadgeDTO badgeDTO) {
        Badge badge = badgeMapper.toEntity(badgeDTO);
        Badge saved = badgeService.createBadge(badge);
        return ResponseEntity.ok(badgeMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BadgeDTO> updateBadgeById(@PathVariable Long id, @RequestBody BadgeDTO badgeDTO) {
        Badge badge = badgeMapper.toEntity(badgeDTO);
        Badge updated = badgeService.updateBadge(id,badge);
        return ResponseEntity.ok(badgeMapper.toDTO(updated));
    }

    @PutMapping("/{id}/expiryDate")
    public ResponseEntity<BadgeDTO> updateExpiryDateById(@PathVariable Long id, @RequestBody BadgeDTO badgeDTO) {
        Badge badge = badgeMapper.toEntity(badgeDTO);
        Badge updated = badgeService.updateExpiryDate(id,badge);
        return ResponseEntity.ok(badgeMapper.toDTO(updated));
    }
}
