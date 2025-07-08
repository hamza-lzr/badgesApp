package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.CompanyDTO;
import com.ram.badgesapp.entities.Company;
import com.ram.badgesapp.mapper.CompanyMapper;
import com.ram.badgesapp.services.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies().stream()
                .map(companyMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        Company company =  companyMapper.toEntity(companyDTO);
        Company saved = companyService.createCompany(company);
        return ResponseEntity.ok(companyMapper.toDTO(saved));

    }
}
