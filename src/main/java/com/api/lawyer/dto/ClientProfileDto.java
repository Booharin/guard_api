package com.api.lawyer.dto;

import com.api.lawyer.model.User;

public class ClientProfileDto extends UserProfileDto {
    
    public ClientProfileDto(User user) {
        super(user);
    }
    
    public ClientProfileDto() {}
}
