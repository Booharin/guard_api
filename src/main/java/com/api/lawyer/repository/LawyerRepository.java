package com.api.lawyer.repository;

import com.api.lawyer.model.UserLawyer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LawyerRepository extends CrudRepository<UserLawyer, Integer> {

    List<UserLawyer> findByLawyerId(Integer id);

    List<UserLawyer> findAll();

    @Modifying
    @Transactional
    @Query(value = "insert into user_lawyer values (?1, ?2)",
            nativeQuery = true)
    void insertLawyer(Integer id, Integer code);
}
