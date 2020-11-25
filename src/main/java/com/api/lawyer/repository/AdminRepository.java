package com.api.lawyer.repository;

import com.api.lawyer.model.UserAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<UserAdmin, Integer> {
    Optional<UserAdmin> findByEmail(String email);
}
