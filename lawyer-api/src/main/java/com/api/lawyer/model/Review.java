package com.api.lawyer.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "review")
@Entity
@Data
public class Review {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer reviewId;
    private String reviewDescription;
    private Double rating;
    private Integer senderId;
    private Integer receiverId;
    public Review() {}
}
