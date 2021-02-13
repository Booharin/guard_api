package com.api.lawyer.controller;

import com.api.lawyer.dto.AppealDto;
import com.api.lawyer.model.Appeal;
import com.api.lawyer.model.City;
import com.api.lawyer.model.User;
import com.api.lawyer.repository.AppealCrudRepository;
import com.api.lawyer.repository.CityRepository;
import com.api.lawyer.repository.UserCityRepository;
import com.api.lawyer.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class contains rest controllers for appeals
 */
@RestController
@RequestMapping("/api/v1/appeal")
public class AppealController {

    private final AppealCrudRepository appealCrudRepository;

    private final UserRepository userRepository;

    private final CityRepository cityRepository;
    
    private final UserCityRepository userCityRepository;

    public AppealController(AppealCrudRepository appealCrudRepository,
                            UserRepository userRepository,
                            CityRepository cityRepository,
                            UserCityRepository userCityRepository) {
        this.appealCrudRepository = appealCrudRepository;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.userCityRepository = userCityRepository;
    }

    /**
     * Controller gets all appeals based on client id
     *
     * @param id client's id
     * @return array of appeals
     * <p>
     * .../api/v1/appeal/client?id=1
     */
    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public List<AppealDto> getAllAppealsById(@RequestParam Integer id) {
        List<Appeal> appealList = appealCrudRepository.findAllByClientId(id);
        Optional<User> user = userRepository.findUserById(id);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        City city = cityRepository.findCityByCityCode(userCityRepository.findAllByUserId(user.get().getId()).get(0).getCityCode());
        return appealList
                .stream()
                .map(it -> new AppealDto(it, city.getTitle()))
                .collect(Collectors.toList());
    }

    /**
     * Controllers saves appeal
     *
     * @param newAppeal appeal to save
     *
     * .../api/v1/appeal/save
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveAppeal(@RequestBody Appeal newAppeal) {
        Optional<User> optionalUser = userRepository.findById(newAppeal.getClientId());
        optionalUser.ifPresentOrElse(user -> {
            Appeal appeal = new Appeal();
            appeal.setCityCode(newAppeal.getCityCode());
            appeal.setClientId(newAppeal.getClientId());
            appeal.setTitle(newAppeal.getTitle());
            appeal.setAppealDescription(newAppeal.getAppealDescription());
            appeal.setDateCreated(newAppeal.getDateCreated());
            appeal.setSubIssueCode(newAppeal.getSubIssueCode());
            appealCrudRepository.save(appeal);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        });
    }

    /**
     * Controller removes appeal
     *
     * @param appealId appeal's id to remove
     * .../api/v1/appeal/remove?appealId=1
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public void removeAppeal(@RequestParam Integer appealId) {
        Optional<Appeal> appealToRemove = appealCrudRepository.findById(appealId);
        appealToRemove.ifPresentOrElse(appealCrudRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appeal not found");
                }
        );
    }

    /**
     * Controller get appeal by id
     *
     * @param appealId appeal's id
     * .../api/v1/appeal/findAppeal?appealId=1
     */
    @RequestMapping(value = "/findAppeal", method = RequestMethod.GET)
    public Appeal findAppeal(@RequestParam Integer appealId) {
        Optional<Appeal> appealToGet = appealCrudRepository.findById(appealId);
        return appealToGet.orElse(null);
    }

    /**
     * Controller edits appeal
     *
     * @param appeal appeal with changes
     *
     *  .../api/v1/appeal/edit
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void editAppeal(@RequestBody Appeal appeal) {
        Optional<Appeal> optionalAppeal = appealCrudRepository.findById(appeal.getId());
        optionalAppeal.ifPresentOrElse(appeal1 -> {
            appeal1.setSubIssueCode(appeal.getSubIssueCode());
            appeal1.setTitle(appeal.getTitle());
            appeal1.setAppealDescription(appeal.getAppealDescription());
            appealCrudRepository.save(appeal1);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appeal not found");
        });
    }

    /**
     * Controller modifies appeal
     *
     * @param appealId   appeal's id for update
     * @param isSelected status of lawyer (boolean)
     *
     *http://localhost:8080/api/v1/appeal/status?appealId=1&isSelected=false
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public void changeLawyerStatusInAppeal(@RequestParam Integer appealId, @RequestParam Boolean isSelected) {
        Optional<Appeal> optionalAppeal = appealCrudRepository.findById(appealId);
        optionalAppeal.ifPresentOrElse(appeal -> {
            appeal.setLawyerChoosed(isSelected);
            appealCrudRepository.save(appeal);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appeal not found");
        });
    }
}
