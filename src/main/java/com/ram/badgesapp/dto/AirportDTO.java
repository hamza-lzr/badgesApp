package com.ram.badgesapp.dto;

import lombok.Data;

@Data
public class AirportDTO {

    private Long id;
    private String IATA;
    private String name;
    private String city;
    private String country;

}
