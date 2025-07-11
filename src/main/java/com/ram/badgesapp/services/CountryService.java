package com.ram.badgesapp.services;

import com.ram.badgesapp.entities.Country;
import com.ram.badgesapp.repos.CountryRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepo countryRepo;
    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    public List<Country> getAllCountries() {
        return countryRepo.findAll();
    }

    public Country getCountryById(Long id) {
        return countryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + id));
    }

    public Country createCountry(Country country) {
        return countryRepo.save(country);
    }

    public void deleteCountry(Long id){
        countryRepo.deleteById(id);
    }

    public Country updateCountry(Long id, Country country){
        Country c = countryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + id));

        if(country.getName() != null) c.setName(country.getName());
        return countryRepo.save(c);
    }


}
