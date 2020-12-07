package com.api.lawyer.dto;

import com.api.lawyer.model.User;

import java.util.List;


public class LawyerProfileDto extends UserProfileDto {

    private List<Integer> issueCodes;

    public LawyerProfileDto(User user) {
        super(user);
    }
    
    public LawyerProfileDto() {}


    public List<Integer> getIssueCodes() {
        return issueCodes;
    }

    public void setIssueCodes(List<Integer> issueCodes) {
        this.issueCodes = issueCodes;
    }
}
