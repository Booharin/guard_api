package com.api.lawyer.repository;

import com.api.lawyer.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
    @Query( value = "select id from user_table join user_lawyer on issue_code in ?1 and user_table.city_code = ?2 and user_table.role = 'ROLE_LAWYER'",
            nativeQuery = true)
    List<Integer> findAllLawyer(List<Integer> issue_code, Integer city_code);
    
    @Query( value = "select * from user_table join appeal on appeal.client_id = user_table.id and appeal.id = ?1",
            nativeQuery = true)
    User findClientByAppealId(Integer appealId);
    
    List<User> findAllByIdIn(List<Integer> id);
    
    List<User> findAllByRole(String role);

    User findFirstByEmail(String email);

    Optional<User> findUserById(Integer id);
    
    Optional<User> findByEmail(String email);
}
