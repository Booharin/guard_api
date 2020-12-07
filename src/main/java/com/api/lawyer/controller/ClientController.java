package com.api.lawyer.controller;

import com.api.lawyer.dto.ClientProfileDto;
import com.api.lawyer.dto.LawyerProfileDto;
import com.api.lawyer.dto.UserProfileDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class contains rest controllers for client
 */
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final UserRepository userRepository;

    private final LawyerRepository lawyerRepository;

    private final UserCityRepository userCityRepository;

    private final CityRepository cityRepository;

    private final ReviewRepository reviewRepository;

    private final IssueRepository issueRepository;

    public ClientController(UserRepository userRepository,
                            UserCityRepository userCityRepository,
                            LawyerRepository lawyerRepository,
                            CityRepository cityRepository,
                            ReviewRepository reviewRepository,
                            IssueRepository issueRepository) {
        this.userRepository = userRepository;
        this.lawyerRepository = lawyerRepository;
        this.userCityRepository = userCityRepository;
        this.issueRepository = issueRepository;
        this.cityRepository = cityRepository;
        this.reviewRepository = reviewRepository;
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

    @GetMapping("/alllawyers")
    public List<LawyerProfileDto> getAllLawyers(@RequestParam String cityTitle) {
        Optional<City> optionalCity = cityRepository.findCityByTitle(cityTitle);
        if (optionalCity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        City city  = optionalCity.get();
        List<User> lawyers = userRepository.findAllByRole("ROLE_LAWYER");
        List<UserCity> q = userCityRepository.findAllByCityCodeAndUserIdIn(city.getCityCode(), lawyers.stream().map(User::getId).collect(Collectors.toList()));
        lawyers = userRepository.findAllByIdIn(q.stream().map(UserCity::getUserId).collect(Collectors.toList()));
        List<LawyerProfileDto> result = lawyers.stream().map(LawyerProfileDto::new).collect(Collectors.toList());
        result.forEach(it -> it.setReviewList(reviewRepository.findAllByReceiverId(it.getId())));
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

            List<Review> reviews = reviewRepository.findAllByReceiverId(it.getId());
            it.setReviewList(reviews);

            List<Integer> issues = lawyerRepository.findByLawyerId(it.getId())
                    .stream()
                    .map(UserLawyer::getIssueCode)
                    .collect(Collectors.toList());
            it.setIssueCodes(issues);

            List<IssueType> issuesType = issueRepository.findAllByIssueCodeIn(issues);
            it.setIssueTypes(issuesType);
        });
        return result;
    }
}
