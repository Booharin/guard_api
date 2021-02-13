package com.api.lawyer.dto;

import com.api.lawyer.model.User;

import java.util.List;


public class LawyerProfileDto extends UserProfileDto {

    private List<Integer> subIssueCodes;

    public LawyerProfileDto(User user) {
        super(user);
    }
    
    public LawyerProfileDto() {}


    public List<Integer> getSubIssueCodes() {
        return subIssueCodes;
    }

    public void setSubIssueCodes(List<Integer> subIssueCodes) {
        this.subIssueCodes = subIssueCodes;
    }
}
