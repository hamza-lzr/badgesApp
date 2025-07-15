package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.CountryDTO;
import com.ram.badgesapp.mapper.CountryMapper;
import com.ram.badgesapp.services.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
@CrossOrigin(origins = "*")
public class CountryController {

    private final CountryService countryService;
    private final CountryMapper countryMapper;
    public CountryController(CountryService countryService, CountryMapper countryMapper) {
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries(){
        return ResponseEntity.ok(countryService.getAllCountries().stream()
                .map(countryMapper::toDTO).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long id){
        return ResponseEntity.ok(countryMapper.toDTO(countryService.getCountryById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCountryById(@PathVariable Long id){
        countryService.deleteCountry(id);
        return ResponseEntity.ok("Country deleted successfully with id: " + id);
    }

    @PostMapping
    public ResponseEntity<CountryDTO> createCountry(@RequestBody CountryDTO countryDTO){
        CountryDTO saved = countryMapper.toDTO(countryService.createCountry(countryMapper.toEntity(countryDTO)));
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDTO> updateCountryById(@PathVariable Long id, @RequestBody CountryDTO countryDTO){
        return ResponseEntity.ok(countryMapper.toDTO(countryService.updateCountry(id, countryMapper.toEntity(countryDTO))));
    }
}
