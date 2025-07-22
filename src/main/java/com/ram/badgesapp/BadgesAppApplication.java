package com.ram.badgesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BadgesAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BadgesAppApplication.class, args);
    }

}
