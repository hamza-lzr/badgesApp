package com.ram.badgesapp.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BadgeDTO {


    private Long id;

    private String code;

    private String issuedDate;

    private String expiryDate;

    private Long companyId;

    private Long employeeId;

    private List<Long> accessListIds;

}
