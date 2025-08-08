package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.UserEntityRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CongeMapperHelper {

    @Autowired
    private UserEntityRepo userRepo;

    @Named("userFromId")
    public UserEntity userFromId(Long id) {
        if (id == null) return null;
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Named("formatLocalDate")
    public String formatLocalDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    @Named("parseLocalDate")
    public LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
    }

    @Named("formatLocalDateTime")
    public String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Named("parseLocalDateTime")
    public LocalDateTime parseLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) return null;
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
    }
}