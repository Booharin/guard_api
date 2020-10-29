package com.api.lawyer.controller;

import com.api.lawyer.model.Review;
import com.api.lawyer.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * Method saves review
     * @param review review to save
     *
     * http://localhost:8080/api/v1/review/save
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    // fixme: recalc rate
    public void saveReview(@RequestBody Review review) {
        reviewRepository.save(review);
    }
}
