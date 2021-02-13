package com.api.lawyer.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "appeal")
@Entity
public class Appeal {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String appealDescription;
    private Timestamp dateCreated;
    private Integer clientId;
    private Integer subIssueCode;
    private Integer cityCode;
    private Boolean isLawyerChoosed;
    
    public Appeal() {}
    
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
    
    public String getAppealDescription() {
        return appealDescription;
    }
    
    public void setAppealDescription(String appealDescription) {
        this.appealDescription = appealDescription;
    }
    
    public Timestamp getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Integer getClientId() {
        return clientId;
    }
    
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
    
    public Integer getSubIssueCode() {
        return subIssueCode;
    }
    
    public void setSubIssueCode(Integer subIssueCode) {
        this.subIssueCode = subIssueCode;
    }
    
    public Integer getCityCode() {
        return cityCode;
    }
    
    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
    
    public Boolean getLawyerChoosed() {
        return isLawyerChoosed;
    }
    
    public void setLawyerChoosed(Boolean lawyerChoosed) {
        isLawyerChoosed = lawyerChoosed;
    }
}
