package com.api.lawyer.service;

import com.api.lawyer.model.User;

public interface UserService {

    User findUserByUserEmail(String email);
    String writeMessage();
}
