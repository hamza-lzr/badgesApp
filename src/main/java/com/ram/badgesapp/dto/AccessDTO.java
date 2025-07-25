package com.ram.badgesapp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccessDTO {

    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long badgeId;
    private Long airportId;

}
