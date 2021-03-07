package com.api.lawyer.dto;

import com.api.lawyer.model.Appeal;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AppealDto {
    private Integer id;
    private String title;
    private String appealDescription;
    private Timestamp dateCreated;
    private Integer clientId;
    private Integer subIssueCode;
    private String cityTitle;
    private Boolean isLawyerChoosed;
    private BigDecimal cost;

    public AppealDto(Appeal appeal, String cityTitle) {
        this.id = appeal.getId();
        this.title = appeal.getTitle();
        this.appealDescription = appeal.getAppealDescription();
        this.dateCreated = appeal.getDateCreated();
        this.clientId = appeal.getClientId();
        this.subIssueCode = appeal.getSubIssueCode();
        this.isLawyerChoosed = appeal.getLawyerChoosed();
        this.cityTitle = cityTitle;
        this.cost = appeal.getCost();
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

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public Boolean getLawyerChoosed() {
        return isLawyerChoosed;
    }

    public void setLawyerChoosed(Boolean lawyerChoosed) {
        isLawyerChoosed = lawyerChoosed;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
