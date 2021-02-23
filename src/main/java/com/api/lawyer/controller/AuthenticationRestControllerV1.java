package com.api.lawyer.controller;

import com.api.lawyer.dto.AuthenticationRequestDto;
import com.api.lawyer.dto.ClientProfileDto;
import com.api.lawyer.dto.UserProfileDto;
import com.api.lawyer.model.*;
import com.api.lawyer.repository.*;
import com.api.lawyer.security.jwt.JwtTokenProvider;
import com.api.lawyer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final ReviewRepository reviewRepository;
    private final SubIssueRepository subIssueRepository;
    private final LawyerRepository lawyerRepository;
    private final UserCityRepository userCityRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager,
                                          JwtTokenProvider jwtTokenProvider,
                                          CityRepository cityRepository,
                                          UserCityRepository userCityRepository,
                                          ReviewRepository reviewRepository,
                                          SubIssueRepository subIssueRepository,
                                          LawyerRepository lawyerRepository,
                                          UserService userService,
                                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.lawyerRepository = lawyerRepository;
        this.subIssueRepository = subIssueRepository;
        this.reviewRepository = reviewRepository;
        this.userCityRepository = userCityRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String userEmail = requestDto.getUserEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, requestDto.getUserPassword()));
            User user = userService.findUserByUserEmail(userEmail);
            if (user == null) {
                throw new UsernameNotFoundException("User with userEmail: " + userEmail + " not found");
            }
            UserProfileDto userProfileDto = new UserProfileDto(user);
            List<Integer> cityCodes = userCityRepository
                    .findAllByUserId(user.getId())
                    .stream()
                    .map(UserCity::getCityCode)
                    .collect(Collectors.toList());
            userProfileDto.setCityCode(cityCodes);
            Set<Integer> countryCodes = new HashSet<>();
            cityCodes.forEach(it -> {
                countryCodes.add(cityRepository.findCityByCityCode(it).getCountryCode());
            });
            userProfileDto.setCountryCode(new ArrayList<>(countryCodes));
            List<Review> reviews = reviewRepository.findAllByReceiverId(user.getId());
            userProfileDto.setReviewList(reviews);
            if (user.getRole().equals("ROLE_LAWYER")) {
                List<SubIssueType> subIssueTypeList = lawyerRepository
                        .findByLawyerId(user.getId())
                        .stream()
                        .map(UserLawyer::getSubIssueCode)
                        .map(subIssueRepository::findBySubIssueCode)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
                userProfileDto.setSubIssueTypes(subIssueTypeList);
            }

            if (requestDto.getTokenDevice() != null && !requestDto.getTokenDevice().isEmpty()) {
                user.setTokenDevice(requestDto.getTokenDevice());
                userRepository.save(user);
            }

            String token = jwtTokenProvider.createToken(userEmail, user);
            Map<Object, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userProfileDto);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
