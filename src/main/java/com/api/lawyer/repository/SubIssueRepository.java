package com.api.lawyer.repository;

import com.api.lawyer.model.SubIssueType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubIssueRepository extends CrudRepository<SubIssueType, Integer> {

    List<SubIssueType> findAllByIssueCode(Integer issueCode);
    
    Optional<SubIssueType> findBySubIssueCode(Integer issueCode);
    
    Optional<SubIssueType> findByTitle(String title);
}
