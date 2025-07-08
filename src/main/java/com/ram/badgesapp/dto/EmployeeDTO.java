package com.ram.badgesapp.dto;

import com.ram.badgesapp.entities.Status;
import lombok.Data;

@Data
public class EmployeeDTO {

    private Long id;
    private String matricule;
    private String firstName;
    private String lastName;
    private String phone;
    private String status;
    private Long companyId;
    private Long badgeId;

}
