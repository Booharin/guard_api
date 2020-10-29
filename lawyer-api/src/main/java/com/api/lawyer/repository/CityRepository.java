package com.api.lawyer.repository;

import com.api.lawyer.model.City;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Integer> {
    Optional<City> findCityByTitle(String title);
    City findCityByCityCode(Integer code);
    List<City> findAllByCountryCode(Integer code);
}
