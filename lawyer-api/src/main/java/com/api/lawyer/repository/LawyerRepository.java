package com.api.lawyer.repository;

import com.api.lawyer.model.Lawyer;
import org.springframework.data.repository.CrudRepository;

public interface LawyerRepository extends CrudRepository<Lawyer, Integer> {
}
