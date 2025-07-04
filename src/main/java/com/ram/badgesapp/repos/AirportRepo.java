package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepo extends JpaRepository<Airport, Long> {
}
