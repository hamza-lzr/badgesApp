package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestsRepo extends JpaRepository<Requests, Long> {
    List<Requests> findAllByUser_Id(Long userId);
}
