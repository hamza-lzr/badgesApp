package com.ram.badgesapp.controllers;


import com.ram.badgesapp.dto.AccessDTO;
import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.mapper.AccessMapper;
import com.ram.badgesapp.services.AccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access")
@CrossOrigin(origins = "*")
public class AccessController {

    private final AccessService accessService;
    private final AccessMapper accessMapper;
    public AccessController(AccessService accessService, AccessMapper accessMapper) {
        this.accessService = accessService;
        this.accessMapper = accessMapper;
    }

    @GetMapping
    public ResponseEntity<List<AccessDTO>> getAllAccesses() {
        return ResponseEntity.ok(accessService.getAllAccesses().stream()
                .map(accessMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessDTO> getAccessById(@PathVariable Long id) {
        return ResponseEntity.ok(accessMapper.toDTO(accessService.getAccessById(id)));
    }

    @GetMapping("/badge/{id}")
    public ResponseEntity<List<AccessDTO>> getAccessesByBadgeId(@PathVariable Long id) {
        return ResponseEntity.ok(accessService.getAccessesByBadgeId(id).stream()
                .map(accessMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/airport/{id}")
    public ResponseEntity<List<AccessDTO>> getAccessesByAirportId(@PathVariable Long id) {
        return ResponseEntity.ok(accessService.getAccessesByAirportId(id).stream()
                .map(accessMapper::toDTO)
                .toList()
        );
    }

    @PostMapping
    public ResponseEntity<AccessDTO> createAccess(@RequestBody AccessDTO accessDTO) {
        Access access = accessMapper.toEntity(accessDTO);
        return ResponseEntity.ok(accessMapper.toDTO(accessService.addAccess(access)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessDTO> updateAccessById(@PathVariable Long id, @RequestBody AccessDTO accessDTO) {
        Access access = accessMapper.toEntity(accessDTO);
        return ResponseEntity.ok(accessMapper.toDTO(accessService.updateAccess(id, access)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccessById(@PathVariable Long id) {
        accessService.removeAccess(id);
        return ResponseEntity.noContent().build();
    }
}
