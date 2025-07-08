package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("EMPLOYEE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends UserEntity {

    @Column(unique = true, nullable = false)
    private String matricule;

    private String firstName;

    private String lastName;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "badge_id")
    private Badge badge;

    @Enumerated(EnumType.STRING)
    private Status status;

}

