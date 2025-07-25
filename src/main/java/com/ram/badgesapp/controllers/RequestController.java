package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.RequestsDTO;
import com.ram.badgesapp.entities.ReqStatus;
import com.ram.badgesapp.entities.ReqType;
import com.ram.badgesapp.entities.Requests;
import com.ram.badgesapp.entities.UserEntity;
import com.ram.badgesapp.mapper.RequestsMapper;
import com.ram.badgesapp.repos.UserEntityRepo;
import com.ram.badgesapp.services.RequestsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestsService reqService;
    private final RequestsMapper reqMapper;
    private final UserEntityRepo userEntityRepo;
    public RequestController(RequestsService reqService, RequestsMapper reqMapper, UserEntityRepo userEntityRepo) {
        this.reqService = reqService;
        this.reqMapper = reqMapper;
        this.userEntityRepo = userEntityRepo;
    }

    @GetMapping
    public ResponseEntity<Iterable<RequestsDTO>> getAllRequests() {
        return ResponseEntity.ok(reqService.getAllRequests().stream()
                .map(reqMapper::toDTO)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestsDTO> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(reqMapper.toDTO(reqService.getRequest(id)));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteRequest(@PathVariable Long id) {
        reqService.deleteRequest(id);
        return ResponseEntity.ok("Request deleted with id: " + id);

    }

    @PostMapping
    ResponseEntity<RequestsDTO> addRequest(@RequestBody RequestsDTO req) {
        Requests request = reqMapper.toEntity(req);
        return ResponseEntity.ok(reqMapper.toDTO(reqService.saveRequest(request)));
    }

    @PutMapping("/{id}")
    ResponseEntity<RequestsDTO> updateRequest(@PathVariable Long id, @RequestBody RequestsDTO req) {
        Requests request = reqMapper.toEntity(req);
        return ResponseEntity.ok(reqMapper.toDTO(reqService.updateRequest(id, request)));
    }

    @PutMapping("/update-type/{id}")
    ResponseEntity<RequestsDTO> updateRequestType(@PathVariable Long id, @RequestBody ReqType reqType) {
        return ResponseEntity.ok(reqMapper.toDTO(reqService.updateReqType(id, reqType)));
    }

    @PutMapping("update-status/{id}")
    ResponseEntity<RequestsDTO> updateRequestStatus(@PathVariable Long id, @RequestBody ReqStatus reqStatus) {
        return ResponseEntity.ok(reqMapper.toDTO(reqService.updateReqStatus(id, reqStatus)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public List<RequestsDTO> getMyRequests(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        Long internalUserId = userEntityRepo.findByKeycloakId(keycloakId).getId();

        return reqService.getRequestsByEmployeeId(internalUserId)
                .stream()
                .map(reqMapper::toDTO)
                .toList();
    }

    @PostMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<RequestsDTO> createMyRequest(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody RequestsDTO dto
    ) {
        // ✅ Extract Keycloak ID from token
        String keycloakId = jwt.getSubject();

        // ✅ Get the corresponding internal user entity
        UserEntity user = userEntityRepo.findByKeycloakId(keycloakId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Or throw a custom exception
        }

        // ✅ Convert DTO → Entity
        Requests reqEntity = reqMapper.toEntity(dto);

        // ✅ Assign the logged-in user
        reqEntity.setUser(user);

        // ✅ Save the request
        Requests saved = reqService.saveRequest(reqEntity);

        // ✅ Return DTO
        return ResponseEntity.ok(reqMapper.toDTO(saved));
    }









}
