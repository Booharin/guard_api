package com.api.lawyer.model;

import javax.persistence.*;

@Table(name = "setting")
@Entity
public class Setting {
    
    @Id
    private Integer id;
    private Boolean isPhoneVisible;
    private Boolean isEmailVisible;
    private Boolean isChatEnabled;
    
    public Setting() {}
    
    public Setting(Integer id) {
        this.isPhoneVisible = this.isEmailVisible = this.isChatEnabled = true;
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
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
    
    public void setIsChatEnabled(Boolean chatEnabled) {
        isChatEnabled = chatEnabled;
    }
}
