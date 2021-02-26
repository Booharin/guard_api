package com.api.lawyer.model;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Timestamp dateCreated;
    private Integer negative;

    public Review() {}
    
    public Timestamp getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
    
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

    public Integer getNegative() {
        return negative;
    }

    public void setNegative(Integer negative) {
        this.negative = negative;
    }
}
