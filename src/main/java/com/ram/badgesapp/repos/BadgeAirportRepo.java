package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.BadgeAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeAirportRepo extends JpaRepository<BadgeAirport, Long> {
}
