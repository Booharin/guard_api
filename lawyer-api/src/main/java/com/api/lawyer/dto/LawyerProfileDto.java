package com.api.lawyer.dto;

import com.api.lawyer.model.User;

import java.util.List;

public class LawyerProfileDto extends UserProfileDto {

    public LawyerProfileDto(User user, String cityTitle, String countryTitle) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmailVisible() ? user.getEmail() : null;
        this.phoneNumber = user.getPhoneVisible() ? user.getPhoneNumber() : null;
        this.photo = user.getPhoto();
        this.cityTitle = cityTitle;
        this.countryTitle = countryTitle;
        this.isChatEnabled = user.getIsChatEnabled();
        this.isEmailVisible = user.getIsEmailVisible();
        this.isPhoneVisible = user.getIsPhoneVisible();
    }

}
