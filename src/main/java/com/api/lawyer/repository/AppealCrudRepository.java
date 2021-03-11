package com.api.lawyer.repository;

import com.amazonaws.services.fms.model.App;
import com.api.lawyer.model.Appeal;
import com.api.lawyer.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppealCrudRepository extends CrudRepository<Appeal, Integer> {
    
    List<Appeal> findAllByClientId(Integer clientId, Pageable pageable);
    List<Appeal> findAllByClientId(Integer clientId);

    @Query( value = "select * from appeal a join city c on c.title = ?1 and a.city_code = c.city_code and a.sub_issue_code in ?2",
            nativeQuery = true)
    List<Appeal> findAllByCityTitleAndIssueCodeListIn(String cityTitle, List<Integer> issueCodeList, Pageable pageable);

    List<Appeal> findAll();
    
    List<Appeal> findAllByCityCode(Integer cityCode);
    List<Appeal> findAllByCityCode(Integer cityCode, Pageable pageable);
}