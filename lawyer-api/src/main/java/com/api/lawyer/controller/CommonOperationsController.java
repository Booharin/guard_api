package com.api.lawyer.controller;

import com.api.lawyer.dto.ChangePasswordDto;
import com.api.lawyer.dto.RegistrationDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.*;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/common")
public class CommonOperationsController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Qualifier("getJavaMailSender")
    @Autowired
    private JavaMailSender emailSender;

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody RegistrationDto newUser) {
        assert newUser.getRole().equals("CLIENT") || newUser.getRole().equals("LAWYER");

        if (!newUser.getRole().equals("CLIENT") && !newUser.getRole().equals("LAWYER")) {
            return ResponseEntity.status(400).body("Bad role");
        }

        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setRole(newUser.getRole());
        user.setPassword(newUser.getPassword());
        City city = cityRepository.findCityByTitle(newUser.getCity()).orElseThrow();
        user.setCityCode(city.getCityCode());
        user.setCountryCode(city.getCountryCode());
        user.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        if (user.getRole().equals("LAWYER"))
            lawyerRepository.save(new Lawyer(user.getId()));
        else
            clientRepository.save(new Client(user.getId()));
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto passwordDto) {
        Optional<User> foundUser = userRepository.findUserById(passwordDto.getId());

        if (foundUser.isPresent() && foundUser.get().getPassword().equals(passwordDto.getOldPassword())){
            foundUser.get().setPassword(passwordDto.getNewPassword());
            userRepository.save(foundUser.get());
        } else if (foundUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid old password");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestParam String email) {
        if (!isEmailValid(email))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");

        User user = userRepository.findFirstByEmail(email);

        if (user == null)
            return;

        SimpleMailMessage message = new SimpleMailMessage();
        String password = generatePassword();

        user.setPassword(password);
        userRepository.save(user);

        message.setTo(email);
        message.setSubject("New Password");
        message.setText("Your new password is: " + password);

        this.emailSender.send(message);
    }

    private Boolean isEmailValid(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private String generatePassword() {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
                .build();
        return pwdGenerator.generate(18);
    }

    @GetMapping("/getListOfCountriesAndCities")
    public Map<Country, List<City>> getCountriesAndCities(@RequestParam String locale) {
        Map<Country, List<City>> countryToCity = new HashMap<>();
        List<Country> countries = countryRepository.findAllByLocale(locale);
        if (countries.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid locale");
        countries.forEach((country) -> {
            List<City> cities = cityRepository.findAllByCountryCode(country.getCountryCode());
            countryToCity.put(country, cities);
        });
        return countryToCity;
    }

}
