package com.ram.badgesapp.config;

import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service("securityService")
public class SecurityService {

    @Autowired
    private UserEntityService employeeService;

    public boolean canAccessEmployee(Authentication auth, Long requestedId) {
        // Admins always allowed
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // Employees must only access their own ID
        String keycloakId = ((JwtAuthenticationToken) auth).getToken().getSubject();
        UserEntity employee = employeeService.getUserByKeycloakId(keycloakId);
        return employee != null && employee.getId().equals(requestedId);
    }
}

