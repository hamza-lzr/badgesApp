package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    public UserEntity addUser(UserEntity userEntity) {
        return userRepo.save(userEntity);
    }

    public void deleteUser(Long id) {
        UserEntity userEntity = getUserById(id);
        userRepo.delete(userEntity);

    }

    public UserEntity updateUser(Long id, UserEntity userEntity) {
        UserEntity currentUserEntity = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
            currentUserEntity.setEmail(userEntity.getEmail());
            currentUserEntity.setRole(userEntity.getRole());
            userRepo.save(currentUserEntity);


        return currentUserEntity;

    }

}
