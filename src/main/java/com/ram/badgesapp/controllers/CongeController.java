package com.ram.badgesapp.controllers;


import com.ram.badgesapp.dto.CongeDTO;
import com.ram.badgesapp.mapper.CongeMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.CongeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conge")
@CrossOrigin(origins = "*")
public class CongeController {

    private final CongeService congeService;
    private final CongeMapper congeMapper;
    private final UserEntityRepo userEntityRepo;

    public CongeController(CongeService congeService, CongeMapper congeMapper, UserEntityRepo userEntityRepo) {
        this.congeService = congeService;
        this.congeMapper = congeMapper;
        this.userEntityRepo = userEntityRepo;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CongeDTO>> getAllConges(){
        return ResponseEntity.ok(congeService.getAllConges().stream().map(congeMapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CongeDTO> getCongeById(@PathVariable Long id){
        return ResponseEntity.ok(congeMapper.toDTO(congeService.getCongeById(id).orElseThrow( () -> new EntityNotFoundException("Conge not found for id: " + id))));

    }

    @PostMapping
    public ResponseEntity<CongeDTO> saveConge(@RequestBody CongeDTO congeDTO){
        CongeDTO saved = congeMapper.toDTO(congeService.saveConge(congeMapper.toEntity(congeDTO)));
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConge(@PathVariable Long id){
        congeService.deleteConge(id);
        return ResponseEntity.ok("Conge deleted successfully with id: " + id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CongeDTO> updateConge(@PathVariable Long id, @RequestBody CongeDTO conge){
        congeService.updateConge(id, congeMapper.toEntity(conge));
        return ResponseEntity.ok(congeMapper.toDTO(congeService.getCongeById(id).orElseThrow( () -> new EntityNotFoundException("Conge not found for id: " + id))));
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<CongeDTO> updateStatusToApproved(@PathVariable Long id){
        congeService.updateStatusToApproved(id);
        return ResponseEntity.ok(congeMapper.toDTO(congeService.getCongeById(id).orElseThrow( () -> new EntityNotFoundException("Conge not found for id: " + id))));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<CongeDTO> updateStatusToRejected(@PathVariable Long id){
        congeService.updateStatusToRejected(id);
        return ResponseEntity.ok(congeMapper.toDTO(congeService.getCongeById(id).orElseThrow( () -> new EntityNotFoundException("Conge not found for id: " + id))));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public List<CongeDTO> getMyConges(@AuthenticationPrincipal Jwt jwt){

        String keycloakUserId = jwt.getSubject();
        Long internalUserId = userEntityRepo.findByKeycloakId(keycloakUserId)
                .getId();

        return congeService.getCongesByUserId(internalUserId)
                .stream()
                .map(congeMapper::toDTO)
                .toList();
    }

    private boolean hasRole(Jwt jwt, String role) {
        var roles = jwt.getClaimAsMap("realm_access");
        List<String> roleList = (List<String>) roles.get("roles");
        return roleList.contains(role);
    }

}
