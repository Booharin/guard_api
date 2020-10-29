package com.api.lawyer.repository;

import com.api.lawyer.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    // fixme: should we consider subissue?
    @Query( value = "SELECT id FROM user_table JOIN issue_type ON issue_code in ?1 and user_table.city_code = ?2",
            nativeQuery = true)
    List<Integer> findAllLawyer(List<Integer> issue_code, Integer city_code);
    
    @Query( value = "select * from user_table join appeal on appeal.client_id = user_table.id and appeal.id = ?1",
            nativeQuery = true)
    User findClientByAppealId(Integer appealId);

    @Query(value = "SELECT * FROM user_table where id in ?1", nativeQuery = true)
    List<User> findAllByIds(List<Integer> id);

    Optional<User> findUserByEmail(String email);

    User findFirstByEmail(String email);

    Optional<User> findUserById(Integer id);
}
