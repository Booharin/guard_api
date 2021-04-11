package com.api.lawyer.controller;

import com.api.lawyer.dto.AdminDto;
import com.api.lawyer.dto.UserProfileDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
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
    public void saveCountry(@RequestParam String countryTitleRu,
                            @RequestParam Integer countryCode,
                            @RequestParam String locale,
                            @RequestParam String countryTitleEn) {
        Optional<Country> countryOptional = countryRepository.findCountryByTitle(countryTitleRu);
        if (countryOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Country with same title already exist", null);
        if (countryRepository.existsById(countryCode))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Country with same country code already exist");
        Country country = new Country(countryCode, countryTitleRu, locale, countryTitleEn);
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
    public Map<String, List<UserProfileDto>> getAllLawyers(@RequestParam Integer page, @RequestParam Integer pageSize) {
        List<User> users = userRepository.findAllByRole("ROLE_LAWYER", PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<UserProfileDto> lawyers = users
                .stream()
                .map(UserProfileDto::new)
                .collect(Collectors.toList());
        lawyers.forEach(it -> {
            it.setCityCode(userCityRepository.findAllByUserId(it.getId()).stream().map(UserCity::getCityCode).collect(Collectors.toList()));
            it.setCountryCode(it
                    .getCityCode()
                    .stream()
                    .map(cityRepository::findOptionalCityByCode)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(City::getCountryCode)
                    .collect(Collectors.toList()));
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
    public Map<String, List<UserProfileDto>> getAllClients(@RequestParam Integer page, @RequestParam Integer pageSize) {
        List<User> users = userRepository.findAllByRole("ROLE_CLIENT",PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<UserProfileDto> clients = users
                .stream()
                .map(UserProfileDto::new)
                .collect(Collectors.toList());
        clients.forEach(it -> {
            it.setCityCode(userCityRepository.findAllByUserId(it.getId()).stream().map(UserCity::getCityCode).collect(Collectors.toList()));
            it.setCountryCode(it
                    .getCityCode()
                    .stream()
                    .map(cityRepository::findOptionalCityByCode)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(City::getCountryCode)
                    .collect(Collectors.toList()));
        });
        Map<String, List<UserProfileDto>> resultMap = new HashMap<>();
        resultMap.put("clients", clients);
        return resultMap;
    }

    @PostMapping("deleteCity")
    public void deleteCity(@RequestParam String cityName) {
        Optional<City> city = cityRepository.findCityByTitle(cityName);
        city.ifPresent(cityRepository::delete);
    }


    @PostMapping("deleteCountry")
    public void deleteCountry(@RequestParam String countryName) {
        Optional<Country> country = countryRepository.findCountryByTitle(countryName);
        country.ifPresent(it -> {
            List<City> list = cityRepository.findAllByCountryCode(it.getCountryCode());
            cityRepository.deleteAll(list);
            countryRepository.delete(it);
        });
    }

    @PostMapping("city")
    public void saveCity(@RequestParam Integer countryCode,
                         @RequestParam Integer cityCode,
                         @RequestParam String cityTitleRu,
                         @RequestParam String cityTitleEn) {
        Optional<City> cityOptional = cityRepository.findCityByTitle(cityTitleRu);
        if (cityOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "City with same title already exist");
        if (cityRepository.existsById(countryCode))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "City with same city code already exist");
        City city = new City(cityCode, cityTitleRu, countryCode, cityTitleEn);
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
        List<Country> resultList = new ArrayList<>(countryRepository.findAll());
        return resultList;
    }

    @PostMapping("save")
    public void saveAdmin(@RequestBody User admin) {
        if (userRepository.findByEmail(admin.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin with same email already exist");
        admin.setRole("ROLE_ADMIN");
        admin.setDateCreated(new Timestamp(new Date().getTime()));
        userRepository.save(admin);
    }

    @GetMapping("admins")
    public List<User> getAllAdmins() {
        return userRepository.findAllByRole("ROLE_ADMIN");
    }

    private ModelAndView createModel(String name) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(name);
        return modelAndView;
    }
}
