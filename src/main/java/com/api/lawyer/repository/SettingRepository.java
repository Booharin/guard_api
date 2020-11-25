package com.api.lawyer.repository;

import com.api.lawyer.model.Setting;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SettingRepository extends CrudRepository<Setting, Integer> {
    
    Optional<Setting> findById(Integer id);
}
