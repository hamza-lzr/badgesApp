package com.ram.badgesapp.dto;

import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.Status;
import lombok.Data;


@Data
public class UserResponse {
    
    private Long id;
    
    private String email;

    private Long employeeId;
    
    private Role role;
    
    private Status status;
    
}