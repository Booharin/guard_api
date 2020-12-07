package com.api.lawyer.repository;

import com.api.lawyer.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
    @Query( value = "select distinct id from user_table join user_lawyer on user_lawyer.issue_code in ?1 and id = user_lawyer.lawyer_id",
            nativeQuery = true)
    List<Integer> findAllLawyer(List<Integer> issue_code);
    
    @Query( value = "select * from user_table join appeal on appeal.client_id = user_table.id and appeal.id = ?1",
            nativeQuery = true)
    User findClientByAppealId(Integer appealId);
    
    List<User> findAllByIdIn(List<Integer> id);
    
    List<User> findAllByRole(String role);

    User findFirstByEmail(String email);

    Optional<User> findUserById(Integer id);

    List<User> findAll();

    @Query( value = "select * from user_table where user_table.role = 'ROLE_LAWYER' and user_table.id = ?1",
            nativeQuery = true)
    Optional<User> findLawyerById(Integer id);
    
    Optional<User> findByEmail(String email);
}
