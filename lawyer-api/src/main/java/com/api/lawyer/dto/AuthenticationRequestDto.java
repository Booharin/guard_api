package com.api.lawyer.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String userEmail;
    private String userPassword;
}
