package com.api.lawyer.dto;

import com.api.lawyer.model.User;


public class LawyerProfileDto extends UserProfileDto {

    public LawyerProfileDto(User user) {
        super(user);
    }
    
    public LawyerProfileDto() {}
}
