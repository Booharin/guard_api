package com.api.lawyer.model;

import javax.persistence.*;

@Table(name = "city")
@Entity
public class City {
    @Id
    private Integer cityCode;
    private String title;
    private Integer countryCode;
    
    public City() {}
    
    public City(Integer cityCode, String title, Integer countryCode){
        this.cityCode = cityCode;
        this.title = title;
        this.countryCode = countryCode;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }
}
