package com.api.lawyer.controller;

import com.api.lawyer.dto.*;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.*;
import com.api.lawyer.utility.MyConstants;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/common")
public class CommonOperationsController {

    private final CityRepository cityRepository;

    private final UserRepository userRepository;

    private final LawyerRepository lawyerRepository;

    private final ClientRepository clientRepository;
    
    private final SettingRepository settingRepository;

    private final JavaMailSender emailSender;
    
    private final CountryRepository countryRepository;
    
    private final IssueRepository issueRepository;
    
    private final SubIssueRepository subIssueRepository;
    
    private final AppealCrudRepository appealCrudRepository;
    
    private final ReviewRepository reviewRepository;
    
    private final UserCityRepository userCityRepository;

    public CommonOperationsController(CityRepository cityRepository,
                                      UserRepository userRepository,
                                      LawyerRepository lawyerRepository,
                                      ClientRepository clientRepository,
                                      SettingRepository settingRepository,
                                      CountryRepository countryRepository,
                                      ReviewRepository reviewRepository,
                                      IssueRepository issueRepository,
                                      SubIssueRepository subIssueRepository,
                                      UserCityRepository userCityRepository,
                                      AppealCrudRepository appealCrudRepository,
                                      @Qualifier("getJavaMailSender") JavaMailSender emailSender) {
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.lawyerRepository = lawyerRepository;
        this.clientRepository = clientRepository;
        this.settingRepository = settingRepository;
        this.reviewRepository = reviewRepository;
        this.countryRepository = countryRepository;
        this.issueRepository = issueRepository;
        this.subIssueRepository = subIssueRepository;
        this.userCityRepository  = userCityRepository;
        this.appealCrudRepository = appealCrudRepository;
        this.emailSender = emailSender;
    }
    
    /**
     * Controller registers new user
     * @param newUser - new user
     *
     * .../api/v1/common/register
     */
    @PostMapping("/register")
    public User registerNewUser(@RequestBody RegistrationDto newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with same email already exist");
        City city = cityRepository.findCityByTitle(newUser.getCity()).stream().findFirst().orElseThrow();
        User user = new User(newUser);
        user.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        userCityRepository.save(new UserCity(user.getId(), city.getCityCode()));
        settingRepository.save(new Setting(user.getId()));
        if (user.getRole().equals("ROLE_CLIENT"))
            clientRepository.save(new Client(user.getId()));
        return user;
    }
    
    /**
     * Controller changes user's password
     * @param passwordDto - new password
     *
     * .../api/v1/common/change
     */
    @PostMapping("/change")
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
    
    /**
     * Controller handles forgotten password
     * @param email - email to send verification message
     *
     * .../api/v1/common/forgot
     */
    @PostMapping("/forgot")
    public void forgotPassword(@RequestParam String email) {
        if (!isEmailValid(email))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        User user = userRepository.findFirstByEmail(email);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        SimpleMailMessage message = new SimpleMailMessage();
        String password = generatePassword();
        user.setPassword(password);
        userRepository.save(user);
        message.setTo(email);
        message.setFrom(MyConstants.MY_EMAIL);
        message.setSubject("New Password");
        message.setText("Your new password is: " + password);
        this.emailSender.send(message);
    }

    @GetMapping("/testmail")
    public void testMail(@RequestParam String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(MyConstants.MY_EMAIL);
        message.setSubject("New Password");
        message.setText("This is test message");
        this.emailSender.send(message);
    }
    
    /**
     * Controller gets all issue and sub issue by locale
     * @param locale - locale to find
     * @return map of issues and sub issue
     */
    @GetMapping("/issues")
    public List<IssueDto> getAllIssues(@RequestParam String locale){
        List<IssueDto> resultList = new ArrayList<>();
        issueRepository.findAllByLocale(locale,Sort.by(Sort.Direction.ASC,"sort")).forEach((it) -> {
            IssueDto issueDto = new IssueDto(it);
            issueDto.setSubIssueTypeList(subIssueRepository.findAllByIssueCode(it.getIssueCode(),Sort.by(Sort.Direction.ASC,"sort")));
            resultList.add(issueDto);
        });
        return resultList;
    }
    
    /**
     * Controller finds countries and cities by locale
     * @param locale - email to send verification message
     *
     */
    @GetMapping("/countryLocal")
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
    
    @GetMapping("/allcountries")
    public List<CountryDto> getAllCountriesAndCities() {
        List<Country> countries = countryRepository.findAll();
        List<CountryDto> countryToCity = countries.stream().map(CountryDto::new).collect(Collectors.toList());
        if (countries.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid locale");
        countryToCity.forEach((country) -> {
            List<City> cities = cityRepository.findAllByCountryCode(country.getCountryCode());
            country.setCities(cities);
        });
        return countryToCity;
    }
    
    /**
     * Controller gets client information by id of his appeal
     *
     * @param appealId appeal's id
     * @return client's information
     * .../api/v1/common/client?appealId=1
     */
    @GetMapping("/client")
    public ClientProfileDto getClient(@RequestParam Integer appealId) {
        User user = userRepository.findClientByAppealId(appealId);
        List<Integer> cityCodes = userCityRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(UserCity::getCityCode)
                .collect(Collectors.toList());
        ClientProfileDto clientProfileDto = new ClientProfileDto(user);
        clientProfileDto.setCityCode(cityCodes);
        Set<Integer> countryCodes = new HashSet<>();
        cityCodes.forEach(it -> {
            countryCodes.add(cityRepository.findCityByCityCode(it).getCountryCode());
        });
        clientProfileDto.setCountryCode(new ArrayList<>(countryCodes));
        List<Review> reviews = reviewRepository.findAllByReceiverId(0);
        clientProfileDto.setReviewList(reviews);
        clientProfileDto.setSubIssueTypes(null);
        return clientProfileDto;
    }
    
    
    /**
     * Controller gets all lawyers based on issues code and city
     *
     * @param issueCode - array of issue
     * @param cityTitle - code of the city
     * @return array of lawyers
     *
     * .../api/v1/lawyers?issue_code=1,2,3&cityTitle=Moscow
     */
    @GetMapping(value = "/lawyers")
    public List<LawyerProfileDto> getAllLawyer(@RequestParam List<Integer> issueCode, @RequestParam String cityTitle, @RequestParam Integer page, @RequestParam Integer pageSize) {
        Optional<City> city = cityRepository.findCityByTitle(cityTitle);
        if (city.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        List<Integer> listUsersByCity = userCityRepository.findAllByCityCode(city.get().getCityCode())
                .stream()
                .map(UserCity::getUserId)
                .collect(Collectors.toList());
        List<Integer> ListLawyersByIssue = userRepository.findAllLawyer(issueCode);
        Set<Integer> listLawyers = new HashSet<Integer>(listUsersByCity);
        listLawyers.retainAll(ListLawyersByIssue);
        List<User> lawyers = userRepository.findAllByIdIn(new ArrayList<Integer>(listLawyers), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "averageRate").and(Sort.by(Sort.Direction.ASC, "id"))));
        List<LawyerProfileDto> result = lawyers.stream().map(LawyerProfileDto::new).collect(Collectors.toList());
        result.forEach(it -> it.setReviewList(reviewRepository.findAllByReceiverId(0)));
        result.forEach(it -> {
            List<Integer> cityCodes = userCityRepository
                    .findAllByUserId(it.getId())
                    .stream()
                    .map(UserCity::getCityCode)
                    .collect(Collectors.toList());
            it.setCityCode(cityCodes);
            Set<Integer> countryCodes = new HashSet<>();
            cityCodes.forEach(ct -> {
                countryCodes.add(cityRepository.findCityByCityCode(ct).getCountryCode());
            });
            it.setCountryCode(new ArrayList<>(countryCodes));
    
            //List<Review> reviews = reviewRepository.findAllByReceiverId(it.getId());
            //it.setReviewList(reviews);

            List<SubIssueType> subIssueTypeList = lawyerRepository
                    .findByLawyerId(it.getId())
                    .stream()
                    .map(UserLawyer::getSubIssueCode)
                    .map(subIssueRepository::findBySubIssueCode)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            it.setSubIssueTypes(subIssueTypeList);
            
            List<Integer> subIssues = lawyerRepository.findByLawyerId(it.getId())
                    .stream()
                    .map(UserLawyer::getSubIssueCode)
                    .collect(Collectors.toList());
            it.setSubIssueCodes(subIssues);
        });
        return result;
    }
    
    /**
     * Controller gets lawyer's profile by id
     *
     * @param id - lawyer's id
     * @return lawyer's profile
     *
     * .../api/v1/lawyer/profile?id=1
     */
    @GetMapping("/lawyer")
    public LawyerProfileDto getLawyer(@RequestParam Integer id) {
        Optional<User> lawyer = userRepository.findById(id);
        if (lawyer.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");
        LawyerProfileDto lawyerProfileDto = new LawyerProfileDto(lawyer.get());
        List<Integer> cityCodes = userCityRepository
                .findAllByUserId(lawyerProfileDto.getId())
                .stream()
                .map(UserCity::getCityCode)
                .collect(Collectors.toList());
        lawyerProfileDto.setCityCode(cityCodes);
        Set<Integer> countryCodes = new HashSet<>();
        cityCodes.forEach(it -> {
            countryCodes.add(cityRepository.findCityByCityCode(it).getCountryCode());
        });
        lawyerProfileDto.setCountryCode(new ArrayList<>(countryCodes));
        List<Review> reviews = reviewRepository.findAllByReceiverId(0);
        lawyerProfileDto.setReviewList(reviews);
        List<SubIssueType> subIssueTypeList = lawyerRepository
                .findByLawyerId(lawyerProfileDto.getId())
                .stream()
                .map(UserLawyer::getSubIssueCode)
                .map(subIssueRepository::findBySubIssueCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        lawyerProfileDto.setSubIssueTypes(subIssueTypeList);
        lawyerProfileDto.setSubIssueCodes(subIssueTypeList.stream().map(SubIssueType::getSubIssueCode).collect(Collectors.toList()));
        return lawyerProfileDto;
    }
    
    /**
     * Controller gets all appeals based on city and issues (don't need to authenticate)
     *
     * @param issueCodeList list of issues
     * @param city - title of city
     * @return array of appeals
     * <p>
     * .../api/v1/appeal/iac?issueCodeList=1,2,3&city=Moscow
     */
    @RequestMapping(value = "/iac", method = RequestMethod.GET)
    public List<AppealDto> getAllAppeal(@RequestParam List<Integer> issueCodeList, @RequestParam String city, @RequestParam Integer page, @RequestParam Integer pageSize) {
        List<Appeal> appealList = appealCrudRepository.findAllByCityTitleAndIssueCodeListIn(city, issueCodeList, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        return appealList
                .stream()
                .map(it -> new AppealDto(it, city))
                .collect(Collectors.toList());
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
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(48, 122)
                .build();
        return pwdGenerator.generate(18);
    }
}
