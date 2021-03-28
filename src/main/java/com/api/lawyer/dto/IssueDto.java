package com.api.lawyer.dto;

import com.api.lawyer.model.IssueType;
import com.api.lawyer.model.SubIssueType;

import java.util.List;

public class IssueDto {
    
    private Integer id;
    private Integer issueCode;
    private String title;
    private String titleEn;
    private String subtitle;
    private String subtitleEn;
    private String locale;
    private List<SubIssueType> subIssueTypeList;
    private byte[] image;
    
    public IssueDto(IssueType issueType) {
        this.id = issueType.getId();
        this.issueCode = issueType.getIssueCode();
        this.title = issueType.getTitle();
        this.subtitle = issueType.getSubtitle();
        this.locale = issueType.getLocale();
        this.subtitleEn = issueType.getSubtitleEn();
        this.titleEn = issueType.getTitleEn();
        this.image = issueType.getImage();
    }
    
    public List<SubIssueType> getSubIssueTypeList() {
        return subIssueTypeList;
    }

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

    public void setSubIssueTypeList(List<SubIssueType> subIssueTypeList) {
        this.subIssueTypeList = subIssueTypeList;
    }
    
    public Integer getId() {
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
}
