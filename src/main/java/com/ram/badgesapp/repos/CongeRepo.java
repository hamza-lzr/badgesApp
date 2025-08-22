package com.ram.badgesapp.repos;


import com.ram.badgesapp.entities.Conge;
import com.ram.badgesapp.entities.StatusConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CongeRepo extends JpaRepository<Conge, Long> {
    
    /**
     * Find all active leave periods (where current date is between startDate and endDate)
     * and status is APPROVED
     * @param currentDate The current date
     * @return List of active leave periods
     */
    @Query("SELECT c FROM Conge c WHERE c.startDate <= ?1 AND c.endDate >= ?1 AND c.status = com.ram.badgesapp.entities.StatusConge.APPROVED")
    List<Conge> findActiveLeaves(LocalDate currentDate);
    
    /**
     * Find all leave periods for a specific user
     * @param userId The user ID
     * @return List of leave periods for the user
     */
    List<Conge> findByUser_Id(Long userId);
    
    /**
     * Find active leave period for a specific user
     * @param userId The user ID
     * @param currentDate The current date
     * @return List of active leave periods for the user
     */
    @Query("SELECT c FROM Conge c WHERE c.user.id = ?1 AND c.startDate <= ?2 AND c.endDate >= ?2 AND c.status = com.ram.badgesapp.entities.StatusConge.APPROVED")
    List<Conge> findActiveLeavesByUserId(Long userId, LocalDate currentDate);

    List<Conge> findAllByUser_Id(Long userId);
}
