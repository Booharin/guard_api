package com.api.lawyer.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "user_table")
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String photo;
    private Integer cityCode;
    private Integer countryCode;
    private Timestamp dateCreated;
    private int averageRate;
    private String role;
    private Boolean isPhoneVisible;
    private Boolean isEmailVisible;
    private Boolean isChatEnabled;
    
    public User() {}
    

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getPhoneVisible() {
        return isPhoneVisible;
    }

    public void setPhoneVisible(Boolean phoneVisible) {
        isPhoneVisible = phoneVisible;
    }

    public Boolean getEmailVisible() {
        return isEmailVisible;
    }

    public void setEmailVisible(Boolean emailVisible) {
        isEmailVisible = emailVisible;
    }

    public Boolean getChatEnabled() {
        return isChatEnabled;
    }

    public void setChatEnabled(Boolean chatEnabled) {
        isChatEnabled = chatEnabled;
    }

    public Integer getId() {
        return id;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public int getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(int averageRate) {
        this.averageRate = averageRate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
