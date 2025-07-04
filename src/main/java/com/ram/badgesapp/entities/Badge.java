package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private Date issuedDate;

    private Date expiryDate;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BadgeAirport> accesList = new ArrayList<>();

    @ManyToOne
    private Company company;

    @OneToOne(mappedBy = "badge")
    private Employee employee;



}
