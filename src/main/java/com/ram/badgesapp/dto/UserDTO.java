package com.ram.badgesapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {


        private Long id;  // important pour update

        private String email;  // hérité de UserEntity
        private String role;   // hérité de UserEntity
        private String password; // optionnel, ou géré ailleurs

        private String matricule;
        private String firstName;
        private String lastName;
        private String phone;
        private String status;

        private Long companyId;
        private List<Long> BadgesIds ;



}
