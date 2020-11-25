package com.api.lawyer.controller;

import com.api.lawyer.dto.ClientProfileDto;
import com.api.lawyer.model.User;
import com.api.lawyer.model.UserCity;
import com.api.lawyer.repository.UserCityRepository;
import com.api.lawyer.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Class contains rest controllers for client
 */
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final UserRepository userRepository;

    private final UserCityRepository userCityRepository;

    public ClientController(UserRepository userRepository, UserCityRepository userCityRepository) {
        this.userRepository = userRepository;
        this.userCityRepository = userCityRepository;
    }
    

    /**
     * Controller edits client's profile
     *
     * @param profile client's profile
     *
     * .../api/v1/client/profile
     */
    @PostMapping("/edit")
    public void editClientProfile(@RequestBody ClientProfileDto profile) {
        Optional<User> client = userRepository.findUserById(profile.getId());
        client.ifPresentOrElse(it -> {
            it.setEmail(profile.getEmail());
            it.setFirstName(profile.getFirstName());
            it.setLastName(profile.getLastName());
            it.setPhoneNumber(profile.getPhoneNumber());
            it.setPhoto(profile.getPhoto());
            List<UserCity> userCityList = userCityRepository.findAllByUserId(it.getId());
            int index = 0;
            for(UserCity us: userCityList){
                us.setCityCode(profile.getCityCode().get(index++));
                userCityRepository.save(us);
            }
            userRepository.save(it);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        });
    }
}
