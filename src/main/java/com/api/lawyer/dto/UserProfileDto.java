package com.api.lawyer.dto;

import com.api.lawyer.model.IssueType;
import com.api.lawyer.model.Review;
import com.api.lawyer.model.SubIssueType;
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
    protected String role;
    protected Double averageRate;
    private Timestamp dateCreated;
    protected List<Review> reviewList;
    private List<SubIssueType> subIssueTypes;
    private List<Integer> cityCode;
    private List<Integer> countryCode;
    private Integer complaint;
    private Boolean isAnonymus;
    private String description;
    
    public UserProfileDto(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.photo = user.getPhoto();
        this.role = user.getRole();
        this.averageRate = user.getAverageRate();
        this.dateCreated = user.getDateCreated();
        this.complaint = user.getComplaint();
        this.isAnonymus = user.getIsAnonymus();
        this.description = user.getDescription();
    }
    
    public UserProfileDto() {}
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
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
    
    public List<SubIssueType> getSubIssueTypes() {
        return subIssueTypes;
    }
    
    public void setSubIssueTypes(List<SubIssueType> subIssueTypes) {
        this.subIssueTypes = subIssueTypes;
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

    public Integer getComplaint() {
        return complaint;
    }

    public void setComplaint(Integer complaint) {
        this.complaint = complaint;
    }

    public Boolean getIsAnonymus() {
        return isAnonymus;
    }

    public void setIsAnonymus(Boolean isAnonymus) {
        this.isAnonymus = isAnonymus;
    }
}
