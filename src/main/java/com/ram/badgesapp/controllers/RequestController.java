package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.RequestsDTO;
import com.ram.badgesapp.entities.ReqStatus;
import com.ram.badgesapp.entities.ReqType;
import com.ram.badgesapp.entities.Requests;
import com.ram.badgesapp.mapper.RequestsMapper;
import com.ram.badgesapp.services.RequestsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestsService reqService;
    private final RequestsMapper reqMapper;
    public RequestController(RequestsService reqService, RequestsMapper reqMapper) {
        this.reqService = reqService;
        this.reqMapper = reqMapper;
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




}
