package com.ram.badgesapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table
@Data
public class Requests {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private ReqStatus reqStatus;

    @ManyToOne
    private Employee employee;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private ReqType reqType;



}
