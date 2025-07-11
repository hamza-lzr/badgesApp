package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;

    private String name;

    @OneToMany(mappedBy = "country")
    private List<City> cities;
}
