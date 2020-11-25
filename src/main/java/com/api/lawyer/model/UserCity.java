package com.api.lawyer.model;

import javax.persistence.*;

@Table(name = "user_city")
@Entity
public class UserCity {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer cityCode;
    
    public UserCity() {}
    
    public UserCity(Integer userId, Integer cityCode) {
        this.userId = userId;
        this.cityCode = cityCode;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getCityCode() {
        return cityCode;
    }
    
    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
}
