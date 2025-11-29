package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, nullable = false)
    private String matricule;

    private String firstName;

    private String lastName;

    private String phone;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "user")
    private List<Badge> badge = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(unique = true)
    private String keycloakId;

}
