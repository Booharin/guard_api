package com.api.lawyer.controller;


import com.api.lawyer.dto.AppealDto;
import com.api.lawyer.model.Appeal;
import com.api.lawyer.model.City;
import com.api.lawyer.model.User;
import com.api.lawyer.repository.AppealCrudRepository;
import com.api.lawyer.repository.CityRepository;
import com.api.lawyer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appeal")
public class AppealController {

    @Autowired
    private AppealCrudRepository appealCrudRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    /**
     * Method returns all appeals based on city and issue (don't need to authenticate)
     *
     * @param clientId id of client
     * @return array of appeals
     * <p>
     * http://localhost:8080/api/v1/appeal/getById?clientId=1
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public List<AppealDto> getAllAppealsById(@RequestParam Integer clientId) {
        List<Appeal> appealList = appealCrudRepository.findAllByClientId(clientId);
        Optional<User> user = userRepository.findUserById(clientId);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        City city = cityRepository.findCityByCityCode(user.get().getCityCode());
        return appealList
                .stream()
                .map(it -> new AppealDto(it, city.getTitle()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/appealsByIssue", method = RequestMethod.GET)
    public List<AppealDto> getAllAppeal(@RequestParam List<Integer> issueCodeList, @RequestParam String cityTitle) {
        List<Appeal> appealList = appealCrudRepository.findAllByCityTitleAndIssueCodeListIn(cityTitle, issueCodeList);
        return appealList
                .stream()
                .map(it -> new AppealDto(it, cityTitle))
                .collect(Collectors.toList());
    }

    /**
     * Method saves appeal
     *
     * @param app appeal to save
     *            <p>
     *            http://localhost:8080/api/v1/appeal/save
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveAppeal(@RequestBody Appeal app) {
        Optional<User> optionalUser = userRepository.findById(app.getClientId());
        optionalUser.ifPresentOrElse(user -> {
            Appeal appeal = new Appeal();
            appeal.setCityCode(user.getCityCode());
            appeal.setClientId(app.getClientId());
            appeal.setTitle(app.getTitle());
            appeal.setAppealDescription(app.getAppealDescription());
            appeal.setDateCreated(app.getDateCreated());
            appeal.setIssueCode(app.getIssueCode());
            appealCrudRepository.save(appeal);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        });
    }

    /**
     * Method removes appeal
     *
     * @param appealId appeal's id to remove
     *                 <p>
     *                 http://localhost:8080/api/v1/appeal/remove?appealId=1
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public void removeAppeal(@RequestParam Integer appealId) {
        Optional<Appeal> appealToRemove = appealCrudRepository.findById(appealId);
        appealToRemove.ifPresentOrElse(appeal -> appealCrudRepository.delete(appeal),
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appeal not found");
                }
        );
    }

    /**
     * Method to modifies appeal
     *
     * @param appeal appeal with changes
     *               <p>
     *               http://localhost:8080/api/v1/appeal/update
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateAppeal(@RequestBody Appeal appeal) {
        Optional<Appeal> optionalAppeal = appealCrudRepository.findById(appeal.getId());
        optionalAppeal.ifPresentOrElse(appeal1 -> {
            appeal1.setIssueCode(appeal.getIssueCode());
            appeal1.setTitle(appeal.getTitle());
            appeal1.setAppealDescription(appeal.getAppealDescription());
            appealCrudRepository.save(appeal1);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appeal not found");
        });
    }

    /**
     * Method to modifies appeal
     *
     * @param appealId   appeal id for update
     * @param isSelected value to change
     *                   <p>
     *                   http://localhost:8080/api/v1/appeal/updateLawyer?appealId=1&isLawyer=false
     */
    @RequestMapping(value = "/updateLawyer", method = RequestMethod.POST)
    public void changeLawyerAppeal(@RequestParam Integer appealId, @RequestParam Boolean isSelected) {
        Optional<Appeal> optionalAppeal = appealCrudRepository.findById(appealId);
        optionalAppeal.ifPresentOrElse(appeal -> {
                    appeal.setLawyerChoosed(isSelected);
                    appealCrudRepository.save(appeal);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appeal not found");
                });
    }
}
