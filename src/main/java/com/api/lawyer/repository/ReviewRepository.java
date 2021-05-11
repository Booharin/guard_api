package com.api.lawyer.repository;

import com.api.lawyer.model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    
    List<Review> findAllByReceiverId(Integer id);
    List<Review> findAllByReceiverId(Integer id, Pageable pageable);

}
