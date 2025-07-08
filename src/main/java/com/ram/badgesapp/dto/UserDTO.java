package com.ram.badgesapp.dto;

import com.ram.badgesapp.entities.Status;
import lombok.Data;


@Data
public class UserDTO {
    
    private Long id;
    
    private String email;

    private String role;
    
    private Status status;

    private String userType;

    
}