package com.ram.badgesapp.repos;


import com.ram.badgesapp.entities.Conge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongeRepo extends JpaRepository<Conge, Long> {
}
