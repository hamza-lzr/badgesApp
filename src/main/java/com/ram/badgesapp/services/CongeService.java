package com.ram.badgesapp.services;


import com.ram.badgesapp.entities.Conge;
import com.ram.badgesapp.repos.CongeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ram.badgesapp.entities.StatusConge.APPROVED;
import static com.ram.badgesapp.entities.StatusConge.REJECTED;

@Service
public class CongeService {

    private final CongeRepo congeRepo;

    public CongeService(CongeRepo congeRepo) {
        this.congeRepo = congeRepo;
    }

    public Optional<Conge> getCongeById(Long id) {
        return congeRepo.findById(id);
    }

    public Conge saveConge(Conge conge) {
        return congeRepo.save(conge);
    }

    public void deleteConge(Long id) {
        congeRepo.deleteById(id);
    }

    public Optional<Conge> updateConge(Long id, Conge conge){
        Conge optionalConge = congeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conge not found with id: " + id));

        if(conge.getDescription() != null) optionalConge.setDescription(conge.getDescription());
        if(conge.getStatus() != null) optionalConge.setStatus(conge.getStatus());
        if(conge.getStartDate() != null) optionalConge.setStartDate(conge.getStartDate());
        if(conge.getEndDate() != null) optionalConge.setEndDate(conge.getEndDate());

        return Optional.of(congeRepo.save(optionalConge));


    }

    public Optional<Conge> updateStatusToApproved(Long id){
        Optional<Conge> optionalConge = congeRepo.findById(id);
        if(optionalConge.isEmpty()) throw new EntityNotFoundException("Conge not found with id: " + id);

        Conge conge = optionalConge.get();
        conge.setStatus(APPROVED);
        return Optional.of(congeRepo.save(conge));
    }

    public Optional<Conge> updateStatusToRejected(Long id){
        Optional<Conge> optionalConge = congeRepo.findById(id);
        if(optionalConge.isEmpty()) throw new EntityNotFoundException("Conge not found with id: " + id);

        Conge conge = optionalConge.get();
        conge.setStatus(REJECTED);
        return Optional.of(congeRepo.save(conge));


    }

    public List<Conge> getAllConges(){
        return congeRepo.findAll();
    }
}
