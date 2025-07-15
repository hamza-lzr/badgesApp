package com.ram.badgesapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private Long id;
    private String message;

    private boolean read;

    private Long userId;

    private LocalDateTime createdAt;
}
