package com.api.lawyer.dto;


public class RegistrationDto {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String city;
    private String role;
    private Boolean isAnonymus;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsAnonymus() {
        return isAnonymus;
    }

    public void setIsAnonymus(Boolean isAnonymus) {
        this.isAnonymus = isAnonymus;
    }
}
