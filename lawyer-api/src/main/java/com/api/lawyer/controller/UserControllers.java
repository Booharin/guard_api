package com.api.lawyer.controller;


import com.api.lawyer.model.User;
import com.api.lawyer.repository.UserRepository;
import com.api.lawyer.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserControllers {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/getSuccess", method = RequestMethod.POST)
    public String  getSuccess() throws JsonProcessingException {
        return userServiceImpl.writeMessage();
    }
}
