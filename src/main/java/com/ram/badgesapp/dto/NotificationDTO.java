package com.ram.badgesapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private String message;

    private boolean read;

    private Long userId;

    private LocalDateTime createdAt;
}
