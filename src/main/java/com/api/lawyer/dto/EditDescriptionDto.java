package com.api.lawyer.dto;

import com.api.lawyer.model.Review;
import com.api.lawyer.model.SubIssueType;
import com.api.lawyer.model.User;

import java.sql.Timestamp;
import java.util.List;

public class EditDescriptionDto {

    protected Integer userId;
    private String description;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
