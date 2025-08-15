package com.ram.badgesapp.services;


import com.ram.badgesapp.entities.Conge;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.StatusConge;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.CongeRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.ram.badgesapp.entities.StatusConge.APPROVED;
import static com.ram.badgesapp.entities.StatusConge.REJECTED;

@Service
@Transactional
public class CongeService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final CongeRepo congeRepo;
    private final NotificationService notificationService;
    private final BadgeLeaveDetectionService badgeLeaveDetectionService;
    private final UserEntityRepo userEntityRepo;

    public CongeService(CongeRepo congeRepo,
                        NotificationService notificationService,
                        BadgeLeaveDetectionService badgeLeaveDetectionService,
                        UserEntityRepo userEntityRepo) {
        this.congeRepo = congeRepo;
        this.notificationService = notificationService;
        this.badgeLeaveDetectionService = badgeLeaveDetectionService;
        this.userEntityRepo = userEntityRepo;
    }

    // ===== Utilitaires de format pour messages =====
    private String fmt(LocalDate d) {
        return d == null ? "" : d.format(DATE_FMT);
    }

    private String fmt(LocalDateTime dt) {
        return dt == null ? "" : dt.format(DATETIME_FMT);
    }

    private void notifyAllAdminsNewConge(Conge conge) {
        List<UserEntity> admins = userEntityRepo.findAllByRole(Role.ADMIN);
        if (admins == null || admins.isEmpty()) return;

        String msg = "Nouvelle demande de congé du : " +
                fmt(conge.getStartDate()) + " au " + fmt(conge.getEndDate()) +
                " (Employé #" + conge.getUser().getId() + ")";

        for (UserEntity admin : admins) {
            notificationService.createNotificationForUser(admin.getId(), msg);
        }
    }

    private void notifyEmployeeOnStatusChange(Conge conge, StatusConge newStatus) {
        Long employeeId = conge.getUser().getId();
        String base = "Votre demande de congé (" + fmt(conge.getStartDate()) +
                " → " + fmt(conge.getEndDate()) + ") a été ";

        String msg = switch (newStatus) {
            case APPROVED -> base + "APPROUVÉE.";
            case REJECTED -> base + "REJETÉE.";
            default -> null;
        };

        if (msg != null) {
            notificationService.createNotificationForUser(employeeId, msg);
        }
    }

    // ===== CRUD =====

    @Transactional(readOnly = true)
    public Optional<Conge> getCongeById(Long id) {
        return congeRepo.findById(id);
    }

    public Conge saveConge(Conge conge) {
        // Assurer createdAt et status par défaut
        if (conge.getCreatedAt() == null) {
            conge.setCreatedAt(LocalDateTime.now());
        }
        if (conge.getStatus() == null) {
            conge.setStatus(StatusConge.PENDING);
        }

        Conge saved = congeRepo.save(conge);

        // Notif à l’employé (accusé de réception) — optionnel
        notificationService.createNotificationForUser(
                saved.getUser().getId(),
                "Nouvelle demande de congé envoyée : " +
                        fmt(saved.getStartDate()) + " → " + fmt(saved.getEndDate()) +
                        " (créée le " + fmt(saved.getCreatedAt()) + ")"
        );

        // Notif à tous les ADMIN
        notifyAllAdminsNewConge(saved);

        return saved;
    }

    public void deleteConge(Long id) {
        congeRepo.deleteById(id);
    }

    public Optional<Conge> updateConge(Long id, Conge patch) {
        Conge entity = congeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conge not found with id: " + id));

        // Conserver l’ancien statut pour détecter PENDING → X
        StatusConge oldStatus = entity.getStatus();

        if (patch.getDescription() != null) entity.setDescription(patch.getDescription());
        if (patch.getStartDate() != null) entity.setStartDate(patch.getStartDate());
        if (patch.getEndDate() != null) entity.setEndDate(patch.getEndDate());
        if (patch.getStatus() != null) entity.setStatus(patch.getStatus());

        Conge saved = congeRepo.save(entity);

        // Si passage de PENDING → APPROVED/REJECTED, notifier l’employé
        if (oldStatus == StatusConge.PENDING && saved.getStatus() != StatusConge.PENDING) {
            notifyEmployeeOnStatusChange(saved, saved.getStatus());

            // Optionnel : logique spécifique à un congé approuvé (désactiver badge, etc.)
            if (saved.getStatus() == StatusConge.APPROVED) {
                badgeLeaveDetectionService.handleApprovedLeave(saved);
            }
        }

        return Optional.of(saved);
    }

    public Optional<Conge> updateStatusToApproved(Long id) {
        Conge conge = congeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conge not found with id: " + id));

        StatusConge old = conge.getStatus();
        conge.setStatus(StatusConge.APPROVED);
        Conge saved = congeRepo.save(conge);

        if (old == StatusConge.PENDING) {
            notifyEmployeeOnStatusChange(saved, StatusConge.APPROVED);
            badgeLeaveDetectionService.handleApprovedLeave(saved); // optionnel
        }

        return Optional.of(saved);
    }

    public Optional<Conge> updateStatusToRejected(Long id) {
        Conge conge = congeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conge not found with id: " + id));

        StatusConge old = conge.getStatus();
        conge.setStatus(StatusConge.REJECTED);
        Conge saved = congeRepo.save(conge);

        if (old == StatusConge.PENDING) {
            notifyEmployeeOnStatusChange(saved, StatusConge.REJECTED);
        }

        return Optional.of(saved);
    }

    @Transactional(readOnly = true)
    public List<Conge> getAllConges() {
        return congeRepo.findAll();
    }
}
