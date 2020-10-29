package com.api.lawyer.repository;

import com.api.lawyer.model.IssueType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends CrudRepository<IssueType, Integer> {

}
