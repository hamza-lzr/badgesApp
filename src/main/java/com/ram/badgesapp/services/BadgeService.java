package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.repos.BadgeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {

    private final BadgeRepo badgeRepo;

    public BadgeService(BadgeRepo badgeRepo){
        this.badgeRepo = badgeRepo;
    }

    public List<Badge> getAllBadges(){
        return badgeRepo.findAll();
    }

    public Badge getBadgeById(Long id){
        return badgeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + id));
    }

    public Badge createBadge(Badge badge){
        return badgeRepo.save(badge);
    }

    public void deleteBadge(Long id){
        badgeRepo.deleteById(id);
    }

    public Badge updateBadge(Long id, Badge badge){
        Badge b = badgeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + id));
        if(badge.getCode() != null){
            b.setCode(badge.getCode());
        }
        if(badge.getIssuedDate() != null){
            b.setIssuedDate(badge.getIssuedDate());
        }
        if(badge.getExpiryDate() != null){
            b.setExpiryDate(badge.getExpiryDate());
        }
        if(badge.getCompany() != null){
            b.setCompany(badge.getCompany());
        }
        if(badge.getUser() != null){
            b.setUser(badge.getUser());
        }
        badgeRepo.save(b);
        return b;
    }

    public Badge updateExpiryDate(Long id, Badge badge){
        Badge b = badgeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + id));
        if(badge.getExpiryDate() != null){
            b.setExpiryDate(badge.getExpiryDate());
        }
        return badgeRepo.save(b);
    }

}
