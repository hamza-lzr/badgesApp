package com.ram.badgesapp.dto;

import com.ram.badgesapp.entities.BadgeStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BadgeDTO {


    private Long id;

    private String code;

    private String issuedDate;

    private String expiryDate;

    private BadgeStatus status;

    private Long companyId;

    private Long userId;

    private List<Long> accessListIds;

}
