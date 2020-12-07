package com.api.lawyer.repository;

import com.api.lawyer.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    Country findCountryByCountryCode(Integer code);
    Optional<Country> findCountryByTitle(String title);
    List<Country> findAllByLocale(String locale);
    List<Country> findAll();
}
