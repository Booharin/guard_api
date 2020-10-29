package com.api.lawyer.repository;

import com.api.lawyer.model.Appeal;
import com.api.lawyer.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AppealCrudRepository extends CrudRepository<Appeal, Integer> {
    
    List<Appeal> findAllByClientId(Integer clientId);
    
    @Query( value = "select * from appeal join city on city.title = ?1 and appeal.city_code = city.city_code and appeal.issue_code in ?2",
            nativeQuery = true)
    List<Appeal> findAllByCityTitleAndIssueCodeListIn(String cityTitle, List<Integer> issueCodeList);
    
}