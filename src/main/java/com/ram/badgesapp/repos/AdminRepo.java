package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Long> {
}
