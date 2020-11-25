package com.api.lawyer.dto;

import com.api.lawyer.model.IssueType;
import com.api.lawyer.model.Review;
import com.api.lawyer.model.User;

import java.sql.Timestamp;
import java.util.List;

public class UserProfileDto {
    
    protected Integer id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected String photo;
    protected Double averageRate;
    private Timestamp dateCreated;
    protected List<Review> reviewList;
    private List<IssueType> issueTypes;
    private List<Integer> cityCode;
    private List<Integer> countryCode;
    
    public UserProfileDto(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.photo = user.getPhoto();
        this.averageRate = user.getAverageRate();
        this.dateCreated = user.getDateCreated();
    }
    
    public UserProfileDto() {}
    
    public List<Integer> getCityCode() {
        return cityCode;
    }
    
    public void setCityCode(List<Integer> cityCode) {
        this.cityCode = cityCode;
    }
    
    public List<Integer> getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(List<Integer> countryCode) {
        this.countryCode = countryCode;
    }
    
    public Timestamp getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public List<IssueType> getIssueTypes() {
        return issueTypes;
    }
    
    public void setIssueTypes(List<IssueType> issueTypes) {
        this.issueTypes = issueTypes;
    }
    
    public List<Review> getReviewList() {
        return reviewList;
    }
    
    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getPhoto() {
        return photo;
    }
    
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
    public Double getAverageRate() {
        return averageRate;
    }
    
    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }
}
