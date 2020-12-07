package com.api.lawyer.controller;

import com.api.lawyer.dto.IssueDto;
import com.api.lawyer.model.IssueType;
import com.api.lawyer.model.SubIssueType;
import com.api.lawyer.repository.IssueRepository;
import com.api.lawyer.repository.SubIssueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    
    @PostMapping("/issue")
    public void saveIssue(@RequestBody IssueType issueType) {
        checkIssue(issueType);
        issueRepository.save(issueType);
    }
    
    @PostMapping("/subIssue")
    public void saveSubIssue(@RequestBody SubIssueType subIssueType) {
        checkSubIssue(subIssueType);
        subIssueRepository.save(subIssueType);
    }
    
    @PostMapping("/eissue")
    public void editIssue(@RequestBody IssueType issueType){
        Optional<IssueType> issueTypeOptional = issueRepository.findById(issueType.getId());
        issueTypeOptional.ifPresentOrElse(it -> {
            it.setIssueCode(issueType.getIssueCode());
            it.setSubtitle(issueType.getSubtitle());
            it.setSubtitleEn(issueType.getSubtitleEn());
            it.setTitle(issueType.getTitle());
            it.setTitleEn(issueType.getTitleEn());
            it.setLocale(issueType.getLocale());
            issueRepository.save(it);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found");});
    }
    
    @PostMapping("/esubissue")
    public void editSubIssue(@RequestBody SubIssueType subIssueType) {
        Optional<SubIssueType> subIssueTypeOptional = subIssueRepository.findById(subIssueType.getId());
        subIssueTypeOptional.ifPresentOrElse(it -> {
            it.setIssueCode(subIssueType.getIssueCode());
            it.setSubIssueCode(subIssueType.getSubIssueCode());
            it.setSubtitle(subIssueType.getSubtitle());
            it.setSubtitleEn(subIssueType.getSubtitleEn());
            it.setTitle(subIssueType.getTitle());
            it.setTitleEn(subIssueType.getTitleEn());
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
        if (subIssueRepository.findBySubIssueCode(subIssueType.getSubIssueCode()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SubIssue with same sub issue code already exist");
        if (subIssueRepository.findByTitle(subIssueType.getTitle()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SubIssue with same title already exist");
    }
    
    private void checkIssue(IssueType issueType) {
        if (issueRepository.findByIssueCode(issueType.getIssueCode()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Issue with same issue code already exist");
        if (issueRepository.findByTitle(issueType.getTitle()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Issue with same title already exist");
    }
}
