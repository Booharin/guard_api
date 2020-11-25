package com.api.lawyer.controller;

import com.api.lawyer.dto.LawyerProfileDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.CityRepository;
import com.api.lawyer.repository.CountryRepository;
import com.api.lawyer.repository.UserCityRepository;
import com.api.lawyer.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1/lawyer")
public class LawyerController {

    private final UserRepository userRepository;
    
    private final UserCityRepository userCityRepository;

    public LawyerController(UserRepository userRepository, UserCityRepository userCityRepository) {
        this.userRepository = userRepository;
        this.userCityRepository = userCityRepository;
    }

    /**
     * Controller edit lawyer profile
     *
     * @param profile - new lawyer's profile
     *
     * .../api/v1/lawyer/edit
     */
    @PostMapping("/edit")
    public void editLawyerProfile(@RequestBody LawyerProfileDto profile) {
        Optional<User> lawyer = userRepository.findUserById(profile.getId());
        lawyer.ifPresentOrElse(it -> {
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
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");});
    }
}
