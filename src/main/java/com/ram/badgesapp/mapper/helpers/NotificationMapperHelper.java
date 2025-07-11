package com.ram.badgesapp.mapper.helpers;


import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapperHelper {

    @Autowired
    private UserEntityRepo userRepo;

    @Named("userFromId")
    public UserEntity userFromId(Long id) {
        if (id == null) return null;
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}

