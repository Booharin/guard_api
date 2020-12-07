package com.api.lawyer.controller;

import com.api.lawyer.bucket.BucketName;
import com.api.lawyer.filestore.FileStore;
import com.api.lawyer.model.User;
import com.api.lawyer.repository.UserRepository;
import com.api.lawyer.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.apache.http.entity.ContentType.*;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final FileStore fileStore;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    
    public UserController(UserServiceImpl userServiceImpl, UserRepository userRepository, FileStore fileStore) {
        this.userServiceImpl = userServiceImpl;
        this.fileStore = fileStore;
        this.userRepository = userRepository;
    }

    /**
     *
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/getSuccess", method = RequestMethod.POST)
    public String  getSuccess() throws JsonProcessingException {
        return userServiceImpl.writeMessage();
    }

    @PostMapping(
            path = "{id}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("id") Integer userProfileId,
                                       @RequestBody MultipartFile file) {
        isFileEmpty(file);
        // 2. If file is an image
        isImage(file);

        // 3. The user exists in our database
        User user = getUserProfileOrThrow(userProfileId);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setPhoto(filename);
            userRepository.save(user);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    @GetMapping("{id}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("id") Integer id) {
        User user = getUserProfileOrThrow(id);

        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getId());

        if (user.getPhoto() == null || user.getPhoto().isEmpty()) {
            return new byte[0];
        }

        return fileStore.download(path, user.getPhoto());
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private User getUserProfileOrThrow(Integer id) {
        return userRepository
                .findUserById(id)
                .orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found"); });
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }
}
