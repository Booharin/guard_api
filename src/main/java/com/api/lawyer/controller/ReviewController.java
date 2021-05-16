package com.api.lawyer.controller;

import com.api.lawyer.model.Review;
import com.api.lawyer.model.User;
import com.api.lawyer.repository.ReviewRepository;
import com.api.lawyer.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    
    private final ReviewRepository reviewRepository;
    
    private final UserRepository userRepository;
    
    public ReviewController(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Controller uploads new review and recalculate average rating
     *
     * @param review review to save
     *
     * http://localhost:8080/api/v1/review/upload
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void saveReview(@RequestBody Review review) {
        Optional<User> optionalUser = userRepository.findById(review.getReceiverId());
        optionalUser.ifPresentOrElse(it -> {
            List<Review> reviewList = reviewRepository.findAllByReceiverId(it.getId());
            Double sumRating = reviewList.stream().mapToDouble(Review::getRating).sum();
            Double newRating = (sumRating + review.getRating())/(reviewList.size() + 1);
            it.setAverageRate(newRating);
            userRepository.save(it);
            reviewRepository.save(review);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");});
    }

    @GetMapping(value = "/getByReceiverId")
    public List<Review> getReview(@RequestParam int receiverId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (page != null && pageSize != null)
            return reviewRepository.findAllByReceiverId(receiverId, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "reviewId")));
        else
            return reviewRepository.findAllByReceiverIdOrderByReviewId(receiverId);
    }


}
