package com.api.lawyer.dto;

import com.api.lawyer.model.User;

public class SettingsDto {
    private Boolean isPhoneVisible;
    private Boolean isEmailVisible;
    private Boolean isChatEnabled;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SettingsDto(User user) {
        this.isPhoneVisible = user.getPhoneVisible();
        this.isChatEnabled = user.getChatEnabled();
        this.isEmailVisible = user.getEmailVisible();
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
