package com.api.lawyer.model;

import javax.persistence.*;

@Table(name = "issue_subtype")
@Entity
public class SubIssueType {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer subIssueCode;
    private String title;
    private String titleEn;
    private String subtitle;
    private String subtitleEn;
    private Integer issueCode;
    private byte[] image;

    public SubIssueType() {}

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

    public Integer getSubIssueCode() {
        return subIssueCode;
    }
    
    public void setSubIssueCode(Integer subIssueCode) {
        this.subIssueCode = subIssueCode;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    
    public Integer getIssueCode() {
        return issueCode;
    }
    
    public void setIssueCode(Integer issue_code) {
        this.issueCode = issue_code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
