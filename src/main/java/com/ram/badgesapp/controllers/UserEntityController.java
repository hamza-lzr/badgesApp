package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.UserDTO;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.UserMapper;
import com.ram.badgesapp.services.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final UserMapper userMapper;
    public UserEntityController(UserEntityService userEntityService, UserMapper userMapper) {
        this.userEntityService = userEntityService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userEntityService.getAllUsers().stream()
                .map(userMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDTO(userEntityService.getUserById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userEntityService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully with id: " + id);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserEntity user = userMapper.toEntity(userDTO);
        UserEntity saved = userEntityService.createUser(user);
        return ResponseEntity.ok(userMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserEntity user = userMapper.toEntity(userDTO);
        UserEntity updated = userEntityService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserDTO> updateUserStatusById(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserEntity user = userMapper.toEntity(userDTO);
        UserEntity updated = userEntityService.updateEmployeeStatus(id, user);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }
    @DeleteMapping("/{id}/badge")
    public ResponseEntity<UserDTO> removeBadgeFromUserById(@PathVariable Long id) {
        UserEntity updated = userEntityService.removeBadgeFromEmployee(id);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }
    @PutMapping("/{id}/badge")
    public ResponseEntity<UserDTO> addOrUpdateUserBadgeById(@PathVariable Long id, @RequestBody Long idBadge) {
        UserEntity updated = userEntityService.addOrUpdateEmployeeBadge(id, idBadge);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @PutMapping("/{id}/company/{idComp}")
    public ResponseEntity<UserDTO> addOrUpdateUserCompanyById(@PathVariable Long id, @PathVariable Long idComp){
        UserEntity updated = userEntityService.addOrUpdateEmployeeCompany(id, idComp);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }
}
