package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.User;
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

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    public User addUser(User user) {
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepo.delete(user);

    }

    public User updateUser(Long id, User user) {
        User currentUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
            currentUser.setEmail(user.getEmail());
            currentUser.setRole(user.getRole());
            currentUser.setStatus(user.getStatus());
            currentUser.setEmployee(user.getEmployee());
            currentUser.setPassword(user.getPassword());
            userRepo.save(currentUser);


        return  currentUser;

    }

}
