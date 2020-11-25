package com.api.lawyer.service.impl;

import com.api.lawyer.model.User;
import com.api.lawyer.repository.UserRepository;
import com.api.lawyer.service.UserService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;
    
    @Override
    public String writeMessage(){
        return "Success";
    }

    @Override
    public User findUserByUserEmail(String email){
        User requiredUser = userRepository.findFirstByEmail(email);
        if (requiredUser == null)
            logger.info("findUserByUserLogin -> No user found by email = {}", email);
        else
            logger.info("findUserByUserLogin -> successfully found user by email {}, user - {}", email, requiredUser);
        return requiredUser;
    }
}
