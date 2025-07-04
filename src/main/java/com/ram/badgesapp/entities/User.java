package com.ram.badgesapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, EMPLOYEE

    @OneToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private Status status;




}
