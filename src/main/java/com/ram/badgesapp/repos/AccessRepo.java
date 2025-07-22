package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccessRepo extends JpaRepository<Access, Long> {
    List<Access> findAllByBadge_Id(Long badgeId);

    List<Access> findAllByAirport_Id(Long airportId);

    List<Access> findByEndDateBetween(LocalDate today, LocalDate oneWeekFromNow);

    List<Access> findAllByBadge_User_Id(Long badgeUserId);
}
