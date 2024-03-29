package com.api.lawyer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "country")
@Entity
public class Country {
    @Id
    private Integer countryCode;
    private String title;
    private String titleEn;
    private String locale;

    public Country() {}
    
    public Country(Integer countryCode, String title, String locale, String titleEn){
        this.countryCode = countryCode;
        this.titleEn = titleEn;
        this.title = title;
        this.locale = locale;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override // fixme: override from hardcoded
    public String toString() {
        return "Country{" +
                "countryCode=" + countryCode +
                ", title='" + title + '\'' +
                ", titleEn='" + titleEn + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
