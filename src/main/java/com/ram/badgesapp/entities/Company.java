package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String description;

    @OneToMany(mappedBy = "company")
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Badge> badges = new ArrayList<>();


}
