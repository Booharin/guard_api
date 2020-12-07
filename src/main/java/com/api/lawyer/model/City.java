package com.api.lawyer.model;

import javax.persistence.*;

@Table(name = "city")
@Entity
public class City {
    @Id
    private Integer cityCode;
    private String title;
    private String titleEn;
    private Integer countryCode;
    
    public City() {}
    
    public City(Integer cityCode, String title, Integer countryCode, String titleEn){
        this.cityCode = cityCode;
        this.title = title;
        this.titleEn = titleEn;
        this.countryCode = countryCode;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
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
