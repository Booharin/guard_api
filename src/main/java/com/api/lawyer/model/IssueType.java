package com.api.lawyer.model;

import javax.persistence.*;

@Table(name = "issue_type")
@Entity
public class IssueType {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer issueCode;
    private String title;
    private String titleEn;
    private String subtitle;
    private String subtitleEn;
    private String locale;
    private byte[] image;
    private Integer sort;

    public IssueType() {}

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getSubtitleEn() {
        return subtitleEn;
    }

    public void setSubtitleEn(String subtitleEn) {
        this.subtitleEn = subtitleEn;
    }


    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIssueCode() {
        return issueCode;
    }
    
    public void setIssueCode(Integer issueCode) {
        this.issueCode = issueCode;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSubtitle() {
        return subtitle;
    }
    
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
