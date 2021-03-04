package com.api.lawyer.controller;

import com.api.lawyer.dto.AppealDto;
import com.api.lawyer.dto.LawyerProfileDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1/lawyer")
public class LawyerController {

    private final UserRepository userRepository;
    
    private final UserCityRepository userCityRepository;

    private final LawyerRepository lawyerRepository;

    private final CityRepository cityRepository;

    private final AppealCrudRepository appealCrudRepository;

    public LawyerController(UserRepository userRepository,
                            UserCityRepository userCityRepository,
                            LawyerRepository lawyerRepository,
                            AppealCrudRepository appealCrudRepository,
                            CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.userCityRepository = userCityRepository;
        this.lawyerRepository = lawyerRepository;
        this.appealCrudRepository = appealCrudRepository;
        this.cityRepository = cityRepository;
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
        Optional<User> lawyer = userRepository.findLawyerById(profile.getId());
        lawyer.ifPresentOrElse(it -> {
            it.setEmail(profile.getEmail());
            it.setFirstName(profile.getFirstName());
            it.setLastName(profile.getLastName());
            it.setPhoneNumber(profile.getPhoneNumber());
            it.setPhoto(profile.getPhoto());
            List<UserCity> userCityList = userCityRepository.findAllByUserId(it.getId());
            userCityRepository.deleteAll(userCityList);
            for(Integer cityCode:  profile.getCityCode()) {
                UserCity us = new UserCity(it.getId(), cityCode);
                userCityRepository.save(us);
            }
            if (profile.getSubIssueCodes() != null) {
            List<UserLawyer> lawyers = lawyerRepository.findByLawyerId(profile.getId());
            lawyerRepository.deleteAll(lawyers);
                for (Integer code : profile.getSubIssueCodes()) {
                    lawyerRepository.insertLawyer(profile.getId(), code);
                }
            }
            userRepository.save(it);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");});
    }
    
    @GetMapping("/allappeal")
    public List<AppealDto> getAllAppeals() {
        List<AppealDto> result = new ArrayList<>();
        appealCrudRepository.findAll().forEach(it -> {
            City city = cityRepository.findCityByCityCode(it.getCityCode());
            result.add(new AppealDto(it, city.getTitle()));
        });
        return  result;
    }
    
    @GetMapping("/allappealcity")
    public List<AppealDto> getAllAppealsByCity(@RequestParam String cityTitle, @RequestParam Integer page, @RequestParam Integer pageSize) {
        List<AppealDto> result = new ArrayList<>();
        Optional<City> optionalCity = cityRepository.findCityByTitle(cityTitle);
        if(optionalCity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        appealCrudRepository.findAllByCityCode(optionalCity.get().getCityCode(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"))).forEach(it -> {
            City city = cityRepository.findCityByCityCode(it.getCityCode());
            result.add(new AppealDto(it, city.getTitle()));
        });
        return  result;
    }
}
