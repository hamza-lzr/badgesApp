package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String IATA;

    private String name;

    private String city;

    private String country;

    @OneToMany(mappedBy = "airport")
    private List<BadgeAirport> badgeAirports = new ArrayList<>();


}
