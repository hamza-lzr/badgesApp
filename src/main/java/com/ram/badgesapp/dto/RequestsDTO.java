package com.ram.badgesapp.dto;

import com.ram.badgesapp.entities.ReqStatus;
import com.ram.badgesapp.entities.ReqType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestsDTO {

    private Long id;

    private String description;

    private ReqStatus reqStatus;

    private Long employeeId;

    private LocalDateTime createdAt;

    private ReqType reqType;

}
