package com.api.lawyer.controller;

import com.api.lawyer.bucket.BucketName;
import com.api.lawyer.dto.ChatMessageDto;
import com.api.lawyer.filestore.FileStore;
import com.api.lawyer.model.User;
import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.repository.ChatMessageRepository;
import com.api.lawyer.repository.UserRepository;
import com.api.lawyer.service.impl.BASE64DecodedMultipartFile;
import com.api.lawyer.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.apache.http.entity.ContentType.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final FileStore fileStore;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public UserController(UserServiceImpl userServiceImpl, UserRepository userRepository, FileStore fileStore) {
        this.userServiceImpl = userServiceImpl;
        this.fileStore = fileStore;
        this.userRepository = userRepository;
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
        String path = String.format("%s/%s", BucketName.IMAGES.getBucketName(), user.getId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setPhoto(filename);
            userRepository.save(user);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    @MessageMapping("/chat/{roomId}/{recieverId}/sendPhotoMessage")
    public void sendPhotoMessage(@DestinationVariable String roomId,
                                 @DestinationVariable String recieverId,
                                 @Payload ChatMessageDto chatMessage) {

        byte[] decoded = Base64.getDecoder().decode(chatMessage.getContent());
        BASE64DecodedMultipartFile file = new BASE64DecodedMultipartFile(decoded, "multipart/form-data");

        isFileEmpty(file);
        // 2. If file is an image
        //isImage(file);

        // create photo message
        ChatMessage mess = new ChatMessage();
        Date date = new Date();
        mess.setContent(chatMessage.getContent());
        mess.setSenderName(chatMessage.getSenderName());
        mess.setChatId(Integer.valueOf(roomId));
        mess.setDateCreated(new Timestamp(date.getTime()));
        mess.setSenderId(chatMessage.getSenderId());
        chatMessageRepository.save(mess);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.IMAGES.getBucketName(), UUID.randomUUID() + "-" + roomId + "_" + recieverId);
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        messagingTemplate.convertAndSend(String.format("/topic/%s", recieverId), mess);

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @MessageMapping("/chat/{roomId}/{recieverId}/{senderName}/{senderId}/sendPhotoMessageCheck")
    public void sendPhotoMessage(@DestinationVariable String roomId,
                                 @DestinationVariable String recieverId,
                                 @DestinationVariable String senderName,
                                 @DestinationVariable Integer senderId,
                                 @Payload MultipartFile file) {

        //isFileEmpty(file);
        // 2. If file is an image
        //isImage(file);

        // create photo message
        ChatMessage mess = new ChatMessage();
        Date date = new Date();
        mess.setContent("check photo");
        mess.setSenderName(senderName);
        mess.setChatId(Integer.valueOf(roomId));
        mess.setDateCreated(new Timestamp(date.getTime()));
        mess.setSenderId(senderId);
        chatMessageRepository.save(mess);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.IMAGES.getBucketName(), UUID.randomUUID() + "-" + roomId + "_" + recieverId);
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        messagingTemplate.convertAndSend(String.format("/topic/%s", recieverId), mess);

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @GetMapping("{id}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("id") Integer id) {
        User user = getUserProfileOrThrow(id);

        String path = String.format("%s/%s",
                BucketName.IMAGES.getBucketName(),
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
