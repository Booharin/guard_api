package com.api.lawyer.controller;

import com.api.lawyer.dto.AdminDto;
import com.api.lawyer.dto.UserProfileDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple controllers for admin
 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    
    private final CountryRepository countryRepository;
    
    private final CityRepository cityRepository;
    
    private final AdminRepository adminRepository;
    
    private final UserRepository userRepository;
    
    private final UserCityRepository userCityRepository;

    public AdminController(UserRepository userRepository,
                           CountryRepository countryRepository,
                           CityRepository cityRepository,
                           UserCityRepository userCityRepository,
                           AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.userCityRepository = userCityRepository;
        this.adminRepository = adminRepository;
    }
    
    @GetMapping("/Directions")
    public ModelAndView directions() {
        return createModel("Directions");
    }
    
    @GetMapping("/Clients")
    public ModelAndView client() {
        return createModel("Clients");
    }
    
    @GetMapping("/PersonalCabinet")
    public ModelAndView cabinet() {
        return createModel("PersonalCabinet");
    }

    @GetMapping("/Lawyers")
    public ModelAndView lawyers() {
        return createModel("Lawyers");
    }

    @GetMapping("/index")
    public ModelAndView index() {
        return createModel("index");
    }

    
    @PostMapping("country")
    public void saveCountry(@RequestParam String countryTitle, @RequestParam Integer countryCode, @RequestParam String locale) {
        Optional<Country> countryOptional = countryRepository.findCountryByTitle(countryTitle);
        if (countryOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Country with same title already exist", null);
        if (countryRepository.existsById(countryCode))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Country with same country code already exist");
        Country country = new Country(countryCode, countryTitle, locale);
        countryRepository.save(country);
    }

    /**
     * Controller gets all lawyer
     *
     * @return map of lawyers
     *
     *
     */
    @GetMapping("/lawyers")
    public Map<String, List<UserProfileDto>> getAllLawyers() {
        List<User> users = userRepository.findAllByRole("ROLE_LAWYER");
        List<UserProfileDto> lawyers = users
                .stream()
                .map(UserProfileDto::new)
                .collect(Collectors.toList());
        lawyers.forEach(it -> {
            it.setCityCode(userCityRepository.findAllByUserId(it.getId()).stream().map(UserCity::getCityCode).collect(Collectors.toList()));
            it.setCountryCode(it.getCityCode().stream().map(cityRepository::findCityByCityCode).map(City::getCountryCode).collect(Collectors.toList()));
        });
        Map<String, List<UserProfileDto>> resultMap = new HashMap<>();
        resultMap.put("lawyers", lawyers);
        return resultMap;
    }

    /**
     * Controller gets all clients
     *
     * @return map of users
     *
     */
    @GetMapping("/clients")
    public Map<String, List<UserProfileDto>> getAllClients() {
        List<User> users = userRepository.findAllByRole("ROLE_CLIENT");
        List<UserProfileDto> clients = users
                .stream()
                .map(UserProfileDto::new)
                .collect(Collectors.toList());
        clients.forEach(it -> {
            it.setCityCode(userCityRepository.findAllByUserId(it.getId()).stream().map(UserCity::getCityCode).collect(Collectors.toList()));
            it.setCountryCode(it.getCityCode().stream().map(cityRepository::findCityByCityCode).map(City::getCountryCode).collect(Collectors.toList()));
        });
        Map<String, List<UserProfileDto>> resultMap = new HashMap<>();
        resultMap.put("clients", clients);
        return resultMap;
    }

    @PostMapping("city")
    public void saveCity(@RequestParam Integer countryCode, @RequestParam Integer cityCode, @RequestParam String cityTitle) {
        Optional<City> cityOptional = cityRepository.findCityByTitle(cityTitle);
        if (cityOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "City with same title already exist");
        if (cityRepository.existsById(countryCode))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "City with same city code already exist");
        City city = new City(cityCode, cityTitle, countryCode);
        cityRepository.save(city);
    }

    @GetMapping("/CaC")
    public Map<String, List<String>> getAllCountriesAndCities() {
        Map<String, List<String>> resultMap = new HashMap<>();
        Iterable<Country> countries = countryRepository.findAll();
        countries.forEach(country -> {
            List<String> cities = new ArrayList<>();
            cityRepository.findAllByCountryCode(country.getCountryCode()).forEach(it -> cities.add(it.getTitle()));
            resultMap.put(country.getTitle(), cities);
        });
        return resultMap;
    }

    @GetMapping("city")
    public List<String> getAllCities() {
        List<String> resultList = new ArrayList<>();
        cityRepository.findAll().forEach((it) -> resultList.add(it.getTitle()));
        return resultList;
    }

    @GetMapping("country")
    public List<Country> getAllCountries() {
        List<Country> resultList = new ArrayList<>();
        countryRepository.findAll().forEach(resultList::add);
        return resultList;
    }

    @PostMapping("save")
    public void saveAdmin(@RequestBody UserAdmin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin with same email already exist");
        adminRepository.save(admin);
    }

    @GetMapping("admins")
    public List<AdminDto> getAllAdmins() {
        List<AdminDto> resultList = new ArrayList<>();
        adminRepository.findAll().forEach(it ->  resultList.add(new AdminDto(it)));
        return resultList;
    }

    private ModelAndView createModel(String name) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(name);
        return modelAndView;
    }
}
