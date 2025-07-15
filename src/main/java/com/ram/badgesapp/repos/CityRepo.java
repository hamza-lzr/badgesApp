package com.ram.badgesapp.repos;

import com.ram.badgesapp.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepo extends JpaRepository<City, Long> {
    List<City> findAllByCountry_Id(Long countryId);
}
