package com.ram.badgesapp.controllers;


import com.ram.badgesapp.dto.AccessDTO;
import com.ram.badgesapp.dto.UserDTO;
import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.AccessMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.AccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/access")
@CrossOrigin(origins = "*")
public class AccessController {

    private final AccessService accessService;
    private final AccessMapper accessMapper;
    private final UserEntityRepo userEntityRepo;

    public AccessController(AccessService accessService, AccessMapper accessMapper, UserEntityRepo userEntityRepo) {
        this.accessService = accessService;
        this.accessMapper = accessMapper;
        this.userEntityRepo = userEntityRepo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AccessDTO>> getAllAccesses() {
        return ResponseEntity.ok(accessService.getAllAccesses().stream()
                .map(accessMapper::toDTO)
                .toList()
        );
    }

    @PreAuthorize("hasRole('ADMIN, EMPLOYEE')")
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

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<List<AccessDTO>> getMyAccesses(@AuthenticationPrincipal Jwt jwt) {
        // 1) Extract Keycloak user UUID from token
        String keycloakId = jwt.getSubject();

        // 2) Lookup your internal UserEntity by that UUID
        Long internalUserId = userEntityRepo.findByKeycloakId(keycloakId)
                .getId();

        // 3) Fetch all Access entities for that user (via their badges)
        List<AccessDTO> dtos = accessService
                .getAccessesByUserId(internalUserId)
                .stream()
                .map(accessMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
