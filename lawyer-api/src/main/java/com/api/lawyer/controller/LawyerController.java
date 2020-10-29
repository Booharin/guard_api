package com.api.lawyer.controller;

import com.api.lawyer.dto.ClientProfileDto;
import com.api.lawyer.dto.LawyerProfileDto;
import com.api.lawyer.dto.SettingsDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.CityRepository;
import com.api.lawyer.repository.CountryRepository;
import com.api.lawyer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lawyer")
public class LawyerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    /**
     * Method returns all lawyers based on city and issue (don't need to authenticate)
     * @param issueCode array of issue
     * @param cityTitle code of the city
     * @return array of lawyers
     *
     * https://.../api/v1/get/lawyer?issue_code=1,2,3&city_code=99
     */
    // fixme
    @RequestMapping(value = "/getLawyer", method = RequestMethod.GET)
    public List<PublicUser> getAllLawyer(@RequestParam List<Integer> issueCode, @RequestParam String cityTitle) {
        Optional<City> city = cityRepository.findCityByTitle(cityTitle);
        if (city.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        List<Integer> lawyerId = userRepository.findAllLawyer(issueCode, city.get().getCityCode());
        List<User> lawyers = userRepository.findAllByIds(lawyerId);
        return lawyers.stream().map(PublicUser::new).collect(Collectors.toList());
    }
    
    @GetMapping("/getLawyerProfile")
    public LawyerProfileDto getLawyer(@RequestParam Integer lawyerId) {
        Optional<User> lawyer = userRepository.findById(lawyerId);
        if (lawyer.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");
        City city = cityRepository.findCityByCityCode(lawyer.get().getCityCode());
        Country country = countryRepository.findCountryByCountryCode(city.getCountryCode());
        return new LawyerProfileDto(lawyer.get(), city.getTitle(), country.getTitle());
    }

    @PostMapping("/editProfile")
    public void editLawyerProfile(@RequestBody LawyerProfileDto profile) {
        Optional<User> lawyer = userRepository.findUserById(profile.getId());
        Optional<City> city = cityRepository.findCityByTitle(profile.getCityTitle());
        Optional<Country> country = countryRepository.findCountryByTitle(profile.getCountryTitle());
        if (city.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        if (country.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found");
        lawyer.ifPresentOrElse(it -> {
            it.setEmail(profile.getEmail());
            it.setCityCode(city.get().getCityCode());
            it.setFirstName(profile.getFirstName());
            it.setLastName(profile.getLastName());
            it.setCountryCode(country.get().getCountryCode());
            it.setPhoneNumber(profile.getPhoneNumber());
            it.setPhoto(profile.getPhoto());

            userRepository.save(it);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");});
    }

    @GetMapping("/getLawyerSettings")
    public SettingsDto getSettings(@RequestParam Integer id) {
        Optional<User> lawyer = userRepository.findUserById(id);
        if (lawyer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");
        }
        return new SettingsDto(lawyer.get());
    }

    @PostMapping("/setLawyerSettings")
    public void setLawyerSettings(@RequestBody SettingsDto settings) {
        Optional<User> lawyer = userRepository.findUserById(settings.getId());

        lawyer.ifPresentOrElse(it -> {
            it.setChatEnabled(settings.getChatEnabled());
            it.setEmailVisible(settings.getEmailVisible());
            it.setPhoneVisible(settings.getPhoneVisible());

            userRepository.save(it);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");});
    }
}
