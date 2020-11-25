package com.api.lawyer.dto;

import com.api.lawyer.model.Setting;

public class SettingsDto {
    
    private Integer id;
    private Boolean isPhoneVisible;
    private Boolean isEmailVisible;
    private Boolean isChatEnabled;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SettingsDto(Setting setting) {
        this.id = setting.getId();
        this.isPhoneVisible = setting.getIsPhoneVisible();
        this.isChatEnabled = setting.getIsChatEnabled();
        this.isEmailVisible = setting.getIsEmailVisible();
    }

    public SettingsDto(){}
    
    public Boolean getIsPhoneVisible() {
        return isPhoneVisible;
    }

    public void setIsPhoneVisible(Boolean phoneVisible) {
        isPhoneVisible = phoneVisible;
    }

    public Boolean getIsEmailVisible() {
        return isEmailVisible;
    }

    public void setIsEmailVisible(Boolean emailVisible) {
        isEmailVisible = emailVisible;
    }

    public Boolean getIsChatEnabled() {
        return isChatEnabled;
    }

    public void setChatEnabled(Boolean chatEnabled) {
        isChatEnabled = chatEnabled;
    }
}
