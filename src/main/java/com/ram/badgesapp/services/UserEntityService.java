package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Badge;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.entities.Role;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.repos.BadgeRepo;
import com.ram.badgesapp.repos.UserEntityRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

        if(user.getCompany() !=  null){
            emp.setCompany(user.getCompany());
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





    private UserEntity getOrCreateUserFromToken(JwtAuthenticationToken auth) {
        String keycloakId = auth.getToken().getSubject();
        UserEntity user = userEntityRepo.findByKeycloakId(keycloakId);

        if (user == null) {
            // User exists in Keycloak but not in our DB, so let's create them.
            user = new UserEntity();
            user.setKeycloakId(keycloakId);

            // Populate user details from JWT claims
            user.setEmail(auth.getToken().getClaimAsString("email"));
            user.setFirstName(auth.getToken().getClaimAsString("given_name"));
            user.setLastName(auth.getToken().getClaimAsString("family_name"));

            // Extract roles to assign the correct role in our system
            Map<String, Object> realmAccess = auth.getToken().getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof Collection) {
                @SuppressWarnings("unchecked")
                Collection<String> roles = (Collection<String>) realmAccess.get("roles");
                if (roles.contains("ADMIN")) {
                    user.setRole(Role.ADMIN);
                } else {
                    user.setRole(Role.EMPLOYEE);
                }
            } else {
                // Default to EMPLOYEE if no roles claim is found
                user.setRole(Role.EMPLOYEE);
            }

            // Note: Other fields like 'matricule', 'phone', 'company', 'status'
            // will be null or their default value. They can be updated later through other app flows.

            return userEntityRepo.save(user);
        }
        return user;
    }

    public UserEntity getCurrentEmployee(JwtAuthenticationToken auth) {
        return getOrCreateUserFromToken(auth);
    }

    public boolean canAccessEmployee(Authentication auth, Long requestedId) {
        // Admins can access anyone
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // Use the JIT-provisioning method to get the current user
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        UserEntity emp = getOrCreateUserFromToken(jwtAuth);

        // The 'emp' object is now guaranteed to be non-null.
        // Allow access only if the requested ID matches the logged-in employee’s ID.
        return emp.getId().equals(requestedId);
    }

    public void ensureAdminExists(String keycloakId) {
        if (userEntityRepo.findByKeycloakId(keycloakId) == null) {
            UserEntity admin = new UserEntity();
            admin.setKeycloakId(keycloakId);
            admin.setEmail("hamza");
            admin.setRole(Role.ADMIN);
            userEntityRepo.save(admin);
        }
    }







}
