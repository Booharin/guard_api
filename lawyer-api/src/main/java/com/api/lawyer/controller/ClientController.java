package com.api.lawyer.controller;

import com.api.lawyer.dto.ClientProfileDto;
import com.api.lawyer.dto.SettingsDto;
import com.api.lawyer.model.City;
import com.api.lawyer.model.Country;
import com.api.lawyer.model.User;
import com.api.lawyer.repository.AppealCrudRepository;
import com.api.lawyer.repository.CityRepository;
import com.api.lawyer.repository.CountryRepository;
import com.api.lawyer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AppealCrudRepository appealCrudRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/getClientProfile")
    public ClientProfileDto getClient(@RequestParam Integer appealId) {
        User user = userRepository.findClientByAppealId(appealId);
        City city = cityRepository.findCityByCityCode(user.getCityCode());
        Country country = countryRepository.findCountryByCountryCode(city.getCountryCode());
        return new ClientProfileDto(user, city.getTitle(), country.getTitle());
    }

    @PostMapping("/editProfile")
    public void editClientProfile(@RequestBody ClientProfileDto profile) {
        Optional<User> client = userRepository.findUserById(profile.getId());
        Optional<City> city = cityRepository.findCityByTitle(profile.getCityTitle());
        Optional<Country> country = countryRepository.findCountryByTitle(profile.getCountryTitle());
        if (city.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        if (country.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found");
        client.ifPresentOrElse(it -> {
            it.setEmail(profile.getEmail());
            it.setCityCode(city.get().getCityCode());
            it.setFirstName(profile.getFirstName());
            it.setLastName(profile.getLastName());
            it.setCountryCode(country.get().getCountryCode());
            it.setPhoneNumber(profile.getPhoneNumber());
            it.setPhoto(profile.getPhoto());
            userRepository.save(it);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        });
    }

    @GetMapping("/getClientSettings")
    public SettingsDto getClientSettings(@RequestParam Integer id) {
        Optional<User> client = userRepository.findUserById(id);
        if (client.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
        return new SettingsDto(client.get());
    }

    @PostMapping("/setClientSettings")
    public void setClientSettings(@RequestBody SettingsDto settings) {
        Optional<User> client = userRepository.findUserById(settings.getId());
        client.ifPresentOrElse(it -> {
            it.setChatEnabled(settings.getChatEnabled());
            it.setEmailVisible(settings.getEmailVisible());
            it.setPhoneVisible(settings.getPhoneVisible());
            userRepository.save(it);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        });
    }
}
