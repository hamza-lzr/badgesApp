package com.ram.badgesapp.controllers;

import com.ram.badgesapp.dto.CityDTO;
import com.ram.badgesapp.mapper.CityMapper;
import com.ram.badgesapp.services.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
@CrossOrigin(origins = "*")
public class CityController {

    private final CityService cityService;
    private final CityMapper cityMapper;
    public CityController(CityService cityService, CityMapper cityMapper) {
        this.cityService = cityService;
        this.cityMapper = cityMapper;
    }

    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllCities(){
        return ResponseEntity.ok(cityService.getAllCities().stream()
                .map(cityMapper::toDTO).toList()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable Long id){
        return ResponseEntity.ok(cityMapper.toDTO(cityService.getCityById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCityById(@PathVariable Long id){
        cityService.deleteCity(id);
        return ResponseEntity.ok("City deleted successfully with id: " + id);
    }

    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO){
        CityDTO saved = cityMapper.toDTO(cityService.createCity(cityMapper.toEntity(cityDTO)));
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> updateCityById(@PathVariable Long id, @RequestBody CityDTO cityDTO){
        return ResponseEntity.ok(cityMapper.toDTO(cityService.updateCity(id, cityMapper.toEntity(cityDTO))));
    }

    @GetMapping("/country-id/{id}")
    public ResponseEntity<List<CityDTO>> getCitiesByCountry(@PathVariable Long id){
        return ResponseEntity.ok(cityService.getCitiesByCountry(id).stream()
                .map(cityMapper::toDTO).toList()
        );
    }
}
