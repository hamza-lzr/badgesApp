package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String matricule;

    private String firstName;

    private String lastName;

    private String phone;

    @ManyToOne
    private Company company;

    @OneToOne
    private Badge badge;

    @OneToOne(mappedBy = "employee")
    private User user;



}
