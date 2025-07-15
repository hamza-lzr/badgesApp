package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.City;
import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.repos.CityRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private final CityRepo cityRepo;
    public CityService(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public List<City> getAllCities() {
        return cityRepo.findAll();
    }

    public City getCityById(Long id) {
        return cityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));
    }

    public City createCity(City city) {
        return cityRepo.save(city);
    }

    public void deleteCity(Long id){
        cityRepo.deleteById(id);
    }

    public City updateCity(Long id, City city){
        City c = cityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));

        if(city.getName() != null) c.setName(city.getName());
        if(city.getCountry() != null) c.setCountry(city.getCountry());
        return cityRepo.save(c);
    }

    public List<City> getCitiesByCountry(Long id){
        return cityRepo.findAllByCountry_Id(id)
                ;
    }


}
