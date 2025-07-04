package com.ram.badgesapp.dto;

import lombok.Data;

@Data
public class EmployeeDTO {

    private Long id;
    private String matricule;
    private String firstName;
    private String lastName;
    private String phone;
    private Long companyId;
    private Long badgeId;
    private Long userId;
}
