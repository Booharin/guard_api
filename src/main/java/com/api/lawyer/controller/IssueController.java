package com.api.lawyer.controller;

import com.api.lawyer.dto.IssueDto;
import com.api.lawyer.model.IssueType;
import com.api.lawyer.model.SubIssueType;
import com.api.lawyer.repository.IssueRepository;
import com.api.lawyer.repository.SubIssueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/issue")
public class IssueController {

    private final IssueRepository issueRepository;
    private final SubIssueRepository subIssueRepository;

    public IssueController(IssueRepository issueRepository, SubIssueRepository subIssueRepository) {
        this.issueRepository = issueRepository;
        this.subIssueRepository = subIssueRepository;
    }

    @ResponseBody
    @RequestMapping(value = "/imageissue", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] imageIssue(@RequestParam Integer id){
        IssueType issueType = issueRepository.findByIssueCode(id).get();
        if (issueType != null)
            return issueType.getImage();
        else
            return null;
    }

    @ResponseBody
    @RequestMapping(value = "/imagesubissue", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] imageSubIssue(@RequestParam Integer id){
        SubIssueType subIssueType = subIssueRepository.findBySubIssueCode(id).get();
        if (subIssueType != null)
            return subIssueType.getImage();
        else
            return null;
    }
    
    @PostMapping("/issue")
    public void saveIssue(@RequestParam String data, @RequestParam(value = "image", required = false) MultipartFile image) throws JsonProcessingException {

        IssueType issue = new ObjectMapper().readValue(data, IssueType.class);
        checkIssue(issue);

        if (image!=null && !image.isEmpty()) {
            try {
                issue.setImage(image.getBytes());
            } catch (Exception e) {
                System.out.println("Ошибка при загрузке изображения");
            }
        } else {
            System.out.println("Нет файла");
        }

        issueRepository.save(issue);
    }
    
    @PostMapping("/subIssue")
    public void saveSubIssue(@RequestParam String data, @RequestParam(value = "image", required = false) MultipartFile image) throws JsonProcessingException {
        SubIssueType subIssueType = new ObjectMapper().readValue(data, SubIssueType.class);
        checkSubIssue(subIssueType);
        if (image!=null && !image.isEmpty()) {
            try {
                subIssueType.setImage(image.getBytes());
            } catch (Exception e) {
                System.out.println("Ошибка при загрузке изображения");
            }
        } else {
            System.out.println("Нет файла");
        }
        subIssueRepository.save(subIssueType);
    }
    
    @PostMapping("/eissue")
    public void editIssue(@RequestParam String data, @RequestParam(value = "image", required = false) MultipartFile image) throws JsonProcessingException {
        IssueType issueType = new ObjectMapper().readValue(data, IssueType.class);
        checkIssue(issueType);
        Optional<IssueType> issueTypeOptional = issueRepository.findById(issueType.getId());
        issueTypeOptional.ifPresentOrElse(it -> {
            it.setIssueCode(issueType.getIssueCode());
            it.setSubtitle(issueType.getSubtitle());
            it.setSubtitleEn(issueType.getSubtitleEn());
            it.setTitle(issueType.getTitle());
            it.setTitleEn(issueType.getTitleEn());
            it.setLocale(issueType.getLocale());
            it.setSort(issueType.getSort());

            if (image!=null && !image.isEmpty()) {
                try {
                    it.setImage(image.getBytes());
                } catch (Exception e) {
                    System.out.println("Ошибка при загрузке изображения");
                }
            } else {
                System.out.println("Нет файла");
            }

            issueRepository.save(it);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found");});
    }
    
    @PostMapping("/esubissue")
    public void editSubIssue(@RequestParam String data, @RequestParam(value = "image", required = false) MultipartFile image) throws JsonProcessingException {
        SubIssueType subIssueType = new ObjectMapper().readValue(data, SubIssueType.class);
        checkSubIssue(subIssueType);
        Optional<SubIssueType> subIssueTypeOptional = subIssueRepository.findById(subIssueType.getId());
        subIssueTypeOptional.ifPresentOrElse(it -> {
            it.setIssueCode(subIssueType.getIssueCode());
            it.setSubIssueCode(subIssueType.getSubIssueCode());
            it.setSubtitle(subIssueType.getSubtitle());
            it.setSubtitleEn(subIssueType.getSubtitleEn());
            it.setTitle(subIssueType.getTitle());
            it.setTitleEn(subIssueType.getTitleEn());
            it.setSort(subIssueType.getSort());

            if (image!=null && !image.isEmpty()) {
                try {
                    it.setImage(image.getBytes());
                } catch (Exception e) {
                    System.out.println("Ошибка при загрузке изображения");
                }
            } else {
                System.out.println("Нет файла");
            }

            subIssueRepository.save(it);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found");});
    }

    @PostMapping("/deleteIssue")
    public void deleteIssue(@RequestParam Integer id) {
        Optional<IssueType> issue = issueRepository.findById(id);
        issue.ifPresent(issueRepository::delete);
    }

    @PostMapping("/deleteSubIssue")
    public void deleteSubIssue(@RequestParam Integer id) {
        Optional<SubIssueType> subIssue = subIssueRepository.findById(id);
        subIssue.ifPresent(subIssueRepository::delete);
    }

    private void checkSubIssue(SubIssueType subIssueType) {
        int currentId = 0;
        if (subIssueType.getId() != null)
            currentId = subIssueType.getId();
        if (subIssueRepository.findBySubIssueCodeAndIdNot(subIssueType.getSubIssueCode(), currentId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SubIssue with same sub issue code already exist");
        if (subIssueRepository.findByTitleAndIdNot(subIssueType.getTitle(), currentId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SubIssue with same title already exist");
    }
    
    private void checkIssue(IssueType issueType) {
        int currentId = 0;
        if (issueType.getId() != null)
            currentId = issueType.getId();

        if (issueRepository.findByIssueCodeAndIdNot(issueType.getIssueCode(), currentId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Issue with same issue code already exist");
        if (issueRepository.findByTitleAndIdNot(issueType.getTitle(), currentId).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Issue with same title already exist");
    }
}
