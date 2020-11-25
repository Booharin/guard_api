package com.api.lawyer.repository;

import com.api.lawyer.model.UserLawyer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LawyerRepository extends CrudRepository<UserLawyer, Integer> {
    
    List<UserLawyer> findByLawyerId(Integer id);
}
