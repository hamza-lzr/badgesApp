package com.ram.badgesapp.mapper.helpers;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.Status;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.CompanyRepo;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperHelper {

    @Autowired
    private BadgeRepo badgeRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Named("companyFromId")
    public Company companyFromId(Long id) {
        if (id == null) return null;
        return companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Named("badgeListFromIds")
    public List<Badge> badgeListFromIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        return ids.stream()
                .map(id -> badgeRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Badge not found with id: " + id)))
                .collect(Collectors.toList());
    }

    @Named("stringToRole")
    public Role stringToRole(String roleStr) {
        if (roleStr == null || roleStr.isEmpty()) return null;
        try {
            return Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }
    }

    @Named("stringToStatus")
    public Status stringToStatus(String statusStr) {
        if (statusStr == null || statusStr.isEmpty()) return null;
        try {
            return Status.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + statusStr);
        }
    }
}
