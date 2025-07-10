package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.BadgeAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeAirportRepo extends JpaRepository<BadgeAirport, Long> {
    List<BadgeAirport> findAllByBadge_Id(Long badgeId);

    List<BadgeAirport> findAllByAirport_Id(Long airportId);
}
