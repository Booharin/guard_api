package com.api.lawyer.repository;

import com.api.lawyer.model.IssueType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends CrudRepository<IssueType, Integer> {

    List<IssueType> findAllByLocale(String locale);
    
    Optional<IssueType> findByIssueCode(Integer issueCode);
    
    Optional<IssueType> findByTitle(String title);
}
