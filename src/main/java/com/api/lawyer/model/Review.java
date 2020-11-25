package com.api.lawyer.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "review")
@Entity
public class Review {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer reviewId;
    private String reviewDescription;
    private Double rating;
    private Integer senderId;
    private Integer receiverId;
    public Review() {}
    
    public Integer getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }
    
    public String getReviewDescription() {
        return reviewDescription;
    }
    
    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public Integer getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    
    public Integer getReceiverId() {
        return receiverId;
    }
    
    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
}
