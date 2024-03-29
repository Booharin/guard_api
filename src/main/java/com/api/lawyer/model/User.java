package com.api.lawyer.model;

import com.api.lawyer.dto.RegistrationDto;

import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "user_table")
@Entity
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
    private Timestamp dateCreated;
    private Double averageRate;
    private String role;
    private String tokenDevice;
    private Integer Complaint;
    private String photobase64;
    private Boolean isAnonymus;
    private String description;
    
    public User() {}
    
    public User(RegistrationDto registrationDto) {
        this.firstName = registrationDto.getFirstName();
        this.lastName = registrationDto.getLastName();
        this.email = registrationDto.getEmail();
        this.password = registrationDto.getPassword();
        this.role = registrationDto.getRole();
        this.averageRate = 0.0;
        this.isAnonymus = registrationDto.getIsAnonymus();
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
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

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTokenDevice() {
        return tokenDevice;
    }

    public void setTokenDevice(String tokenDevice) {
        this.tokenDevice = tokenDevice;
    }

    public Integer getComplaint() {
        return Complaint;
    }

    public void setComplaint(Integer complaint) {
        Complaint = complaint;
    }

    public String getPhotobase64() {
        return photobase64;
    }

    public void setPhotobase64(String photobase64) {
        this.photobase64 = photobase64;
    }

    public Boolean getIsAnonymus() {
        return isAnonymus;
    }

    public void setIsAnonymus(Boolean isAnonymus) {
        this.isAnonymus = isAnonymus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
