package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Access;
import com.ram.badgesapp.repos.AccessRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessService {

    private final AccessRepo accessRepo;
    public AccessService(AccessRepo accessRepo) {
        this.accessRepo = accessRepo;
    }

    public Access addAccess(Access access){
        return accessRepo.save(access);
    }

    public void removeAccess(Long id){
         accessRepo.deleteById(id);
    }

    public Access updateAccess(Long id, Access access){

        Access b = accessRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + id));

        if(access.getStartDate() != null){
            b.setStartDate(access.getStartDate());
        }
        if(access.getEndDate() != null){
            b.setEndDate(access.getEndDate());
        }
        if(access.getAirport() != null){
            b.setAirport(access.getAirport());
        }
        if(access.getBadge() != null){
            b.setBadge(access.getBadge());
        }
        return accessRepo.save(b);

    }

    public Access getAccessById(Long id){
        return accessRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Access not found with id: " + id));
    }

    public List<Access> getAccessesByBadgeId(Long id){
        return accessRepo.findAllByBadge_Id(id);
    }

    public List<Access> getAccessesByAirportId(Long id){
        return accessRepo.findAllByAirport_Id(id);
    }

    public List<Access> getAllAccesses(){
        return accessRepo.findAll();
    }

    public List<Access> getAccessesByUserId(Long id){
        return accessRepo.findAllByBadge_User_Id(id);
    }
}
