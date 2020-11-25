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
    private String subtitle;
    private String locale;

    public IssueType() {}

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
}
