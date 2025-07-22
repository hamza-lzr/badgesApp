package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityService {

    private final UserEntityRepo userEntityRepo;
    private final BadgeRepo badgeRepo;
    private final CompanyService companyService;
    public UserEntityService(UserEntityRepo userEntityRepo, BadgeRepo badgeRepo, CompanyService companyService) {
        this.userEntityRepo = userEntityRepo;
        this.badgeRepo = badgeRepo;
        this.companyService = companyService;
    }

    public UserEntity createUser(UserEntity user) {
        return userEntityRepo.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userEntityRepo.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userEntityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserEntity not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userEntityRepo.deleteById(id);
    }

    public UserEntity updateUser(Long id, UserEntity user){
        UserEntity emp = userEntityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserEntity not found with id: " + id));
        if(user.getFirstName() != null){
            emp.setFirstName(user.getFirstName());
        }
        if(user.getLastName() != null){
            emp.setLastName(user.getLastName());
        }
        if(user.getEmail() != null){
            emp.setEmail(user.getEmail());
        }
        if(user.getPhone() != null){
            emp.setPhone(user.getPhone());
        }
        if(user.getMatricule() != null){
            emp.setMatricule(user.getMatricule());
        }
        if(user.getStatus() != null){
            emp.setStatus(user.getStatus());
        }

        userEntityRepo.save(emp);
        return emp;


    }

    public UserEntity updateEmployeeStatus(Long id, UserEntity employee){
        UserEntity emp = userEntityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        if(employee.getStatus() != null){
            emp.setStatus(employee.getStatus());
        }
        userEntityRepo.save(emp);
        return emp;
    }


    public UserEntity removeBadgeFromEmployee(Long id){
        UserEntity emp = userEntityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        emp.setBadge(null);
        return userEntityRepo.save(emp);
    }
    public UserEntity addOrUpdateEmployeeBadge(Long id, Long idBadge){
        UserEntity emp = userEntityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        Badge badge = badgeRepo.findById(idBadge)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + idBadge));
        emp.getBadge().add(badge);
        return userEntityRepo.save(emp);
    }

    public UserEntity addOrUpdateEmployeeCompany(Long id, Long idComp){
        UserEntity emp = userEntityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        Company company = companyService.getCompanyById(idComp);
        emp.setCompany(company);
        return userEntityRepo.save(emp);
    }

    public UserEntity getUserByKeycloakId(String id){
        return userEntityRepo.findByKeycloakId(id);
    }

    public UserEntity getCurrentEmployee(JwtAuthenticationToken auth) {
        String keycloakSub = auth.getToken().getSubject(); // 'sub' claim
        return userEntityRepo.findByKeycloakId(keycloakSub);
    }

    public boolean canAccessEmployee(Authentication auth, Long requestedId) {
        // Admins can access anyone
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // Get Keycloak user ID from JWT
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        String keycloakSub = jwtAuth.getToken().getSubject();

        // Fetch employee directly
        UserEntity emp = userEntityRepo.findByKeycloakId(keycloakSub);

        // If no employee found → deny access
        if (emp == null) {
            return false;
        }

        // Allow only if the requested ID matches the logged-in employee’s ID
        return emp.getId().equals(requestedId);
    }





}
