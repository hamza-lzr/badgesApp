package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

}
