package com.api.lawyer.repository;

import com.api.lawyer.model.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {
    Optional<City> findCityByTitle(String title);
    
    @Query( value = "select * from city where city_code = ?1",
            nativeQuery = true)
    Optional<City> findOptionalCityByCode(Integer code);
    
    City findCityByCityCode(Integer code);
    List<City> findAllByCountryCode(Integer code);
}
