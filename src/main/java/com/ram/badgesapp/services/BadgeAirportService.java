package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.BadgeAirport;
import com.ram.badgesapp.repos.BadgeAirportRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeAirportService {

    private final BadgeAirportRepo badgeAirportRepo;
    public BadgeAirportService(BadgeAirportRepo badgeAirportRepo) {
        this.badgeAirportRepo = badgeAirportRepo;
    }

    public BadgeAirport addAccess(BadgeAirport badgeAirport){
        return badgeAirportRepo.save(badgeAirport);
    }

    public void removeAccess(Long id){
         badgeAirportRepo.deleteById(id);
    }

    public BadgeAirport updateAccess(Long id, BadgeAirport badgeAirport){

        BadgeAirport b = badgeAirportRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + id));

        if(badgeAirport.getStartDate() != null){
            b.setStartDate(badgeAirport.getStartDate());
        }
        if(badgeAirport.getEndDate() != null){
            b.setEndDate(badgeAirport.getEndDate());
        }
        if(badgeAirport.getAirport() != null){
            b.setAirport(badgeAirport.getAirport());
        }
        if(badgeAirport.getBadge() != null){
            b.setBadge(badgeAirport.getBadge());
        }
        return badgeAirportRepo.save(b);

    }

    public BadgeAirport getAccessById(Long id){
        return badgeAirportRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Access not found with id: " + id));
    }

    public List<BadgeAirport> getAccessesByBadgeId(Long id){
        return badgeAirportRepo.findAllByBadge_Id(id);
    }

    public List<BadgeAirport> getAccessesByAirportId(Long id){
        return badgeAirportRepo.findAllByAirport_Id(id);
    }

    public List<BadgeAirport> getAllAccesses(){
        return badgeAirportRepo.findAll();
    }
}
