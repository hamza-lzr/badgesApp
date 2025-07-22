package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByKeycloakId(String keycloakId);
}
