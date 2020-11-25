package com.api.lawyer.controller;

import com.api.lawyer.dto.SettingsDto;
import com.api.lawyer.model.Setting;
import com.api.lawyer.repository.SettingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {
    
    private final SettingRepository settingRepository;
    
    public SettingController(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }
    
    /**
     * Controller gets user's setting
     *
     * @param id - user's id
     * @return lawyer's setting
     *
     */
    @GetMapping("/settings")
    public SettingsDto getSettings(@RequestParam Integer id) {
        Optional<Setting> setting = settingRepository.findById(id);
        if (setting.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return new SettingsDto(setting.get());
    }
    
    /**
     * Controller sets user's setting
     *
     * @param settings - new user's setting
     *
     */
    @PostMapping("/settings")
    public void setLawyerSettings(@RequestBody SettingsDto settings) {
        Optional<Setting> setting = settingRepository.findById(settings.getId());
        setting.ifPresentOrElse(it -> {
            it.setIsChatEnabled(settings.getIsChatEnabled());
            it.setIsEmailVisible(settings.getIsEmailVisible());
            it.setIsPhoneVisible(settings.getIsPhoneVisible());
            settingRepository.save(it);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");});
    }
}
