package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.BadgeDTO;
import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.mapper.BadgeMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/badges")
@CrossOrigin(origins = "*")
public class BadgeController {

    private final BadgeService badgeService;
    private final BadgeMapper badgeMapper;
    private final UserEntityRepo userEntityRepo;
    public BadgeController(BadgeService badgeService, BadgeMapper badgeMapper, UserEntityRepo userEntityRepo ) {
        this.badgeService = badgeService;
        this.badgeMapper = badgeMapper;
        this.userEntityRepo = userEntityRepo;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BadgeDTO>> getAllBadges() {
        return ResponseEntity.ok(badgeService.getAllBadges().stream()
                .map(badgeMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<BadgeDTO> getBadgeById(@PathVariable Long id, @AuthenticationPrincipal Jwt principal) {
        Badge badge = badgeService.getBadgeById(id);

        // ✅ If EMPLOYEE, ensure they only access their own badge
        if (hasRole(principal, "EMPLOYEE")) {
            Long userIdFromToken = Long.parseLong(principal.getClaimAsString("user_id"));
            if (!badge.getUser().getId().equals(userIdFromToken)) {
                throw new AccessDeniedException("You are not allowed to view this badge");
            }
        }

        return ResponseEntity.ok(badgeMapper.toDTO(badge));
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

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public List<BadgeDTO> getMyBadges(@AuthenticationPrincipal Jwt jwt) {

        // ✅ Get the unique Keycloak user ID (UUID)
        String keycloakUserId = jwt.getSubject(); // same as jwt.getClaimAsString("sub")

        // Now map Keycloak UUID -> your UserEntity.id
        Long internalUserId = userEntityRepo.findByKeycloakId(keycloakUserId)
                .getId();

        return badgeService.getBadgesByUserId(internalUserId)
                .stream()
                .map(badgeMapper::toDTO)
                .toList();
    }



    private boolean hasRole(Jwt jwt, String role) {
        var roles = jwt.getClaimAsMap("realm_access");
        List<String> roleList = (List<String>) roles.get("roles");
        return roleList.contains(role);
    }

}
