package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BadgeRepo extends JpaRepository<Badge, Long> {
    List<Badge> findByExpiryDateBetween(LocalDate today, LocalDate oneWeekFromNow);

    List<Badge> findAllByUser_Id(Long userId);
}
