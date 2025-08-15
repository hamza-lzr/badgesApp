package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface UserEntityRepo extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByRole(Role role); // <- OK avec un enum unique

    UserEntity findByKeycloakId(String keycloakId);
}
