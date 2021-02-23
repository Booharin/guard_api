package com.api.lawyer.dto;

public class PushDto {

    private Integer userId;
    private String message;

    public PushDto() {}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

