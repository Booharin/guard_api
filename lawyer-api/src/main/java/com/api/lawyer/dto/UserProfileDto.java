package com.api.lawyer.dto;

public abstract class UserProfileDto {
    protected Integer id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected String photo;
    protected String cityTitle;
    protected String countryTitle;
    protected int averageRate;
    protected Boolean isPhoneVisible;
    protected Boolean isEmailVisible;
    protected Boolean isChatEnabled;

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

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public int getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(int averageRate) {
        this.averageRate = averageRate;
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
}
