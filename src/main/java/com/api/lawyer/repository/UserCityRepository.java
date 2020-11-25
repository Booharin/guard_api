package com.api.lawyer.repository;

import com.api.lawyer.model.UserCity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCityRepository extends CrudRepository<UserCity, Integer> {
    
    List<UserCity> findAllByUserId(Integer userId);
}
