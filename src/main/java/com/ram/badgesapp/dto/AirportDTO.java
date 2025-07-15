package com.ram.badgesapp.dto;

import lombok.Data;

@Data
public class AirportDTO {

    private Long id;
    private String iata;
    private String name;
    private Long cityId;

}
