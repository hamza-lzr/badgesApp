package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestsRepo extends JpaRepository<Requests, Long> {
}
