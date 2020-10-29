package com.api.lawyer.repository;

import com.api.lawyer.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {
}
