package com.ram.badgesapp.dto;

import com.ram.badgesapp.entities.StatusConge;
import lombok.Data;

@Data
public class CongeDTO {

    private Long id;
    private String startDate;
    private String endDate;
    private Long userId;
    private String createdAt;
    private String description;
    private StatusConge status;
}

