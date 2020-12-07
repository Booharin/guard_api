package com.api.lawyer.dto;

import com.api.lawyer.model.City;
import com.api.lawyer.model.Country;

import java.util.List;

public class CountryDto {
    
    private Integer countryCode;
    private String title;
    private String titleEn;
    private String locale;
    private List<City> cities;
    
    public CountryDto() {}
    
    public CountryDto(Country country) {
        this.title = country.getTitle();
        this.titleEn = country.getTitleEn();
        this.locale = country.getLocale();
        this.countryCode = country.getCountryCode();
    }
    
    public Integer getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitleEn() {
        return titleEn;
    }
    
    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }
    
    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    public List<City> getCities() {
        return cities;
    }
    
    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}

