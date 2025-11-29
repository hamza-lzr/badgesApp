package com.ram.badgesapp;

import com.ram.badgesapp.config.KeycloakAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class BadgesAppApplicationTests {

    @MockitoBean
    private KeycloakAdminService keycloakAdminService;

    @Test
    void contextLoads() {
    }

}
