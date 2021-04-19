package com.api.lawyer.controller;

import com.api.lawyer.bucket.BucketName;
import com.api.lawyer.dto.ChatMessageDto;
import com.api.lawyer.dto.PushDto;
import com.api.lawyer.filestore.FileStore;
import com.api.lawyer.model.Appeal;
import com.api.lawyer.model.User;
import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.ChatRoom;
import com.api.lawyer.repository.AppealCrudRepository;
import com.api.lawyer.repository.ChatMessageRepository;
import com.api.lawyer.repository.ChatRoomRepository;
import com.api.lawyer.repository.UserRepository;
import com.api.lawyer.security.jwt.JwtUser;
import com.api.lawyer.service.impl.BASE64DecodedMultipartFile;
import com.api.lawyer.service.impl.UserServiceImpl;
import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.apache.http.entity.ContentType.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final FileStore fileStore;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final AppealCrudRepository appealCrudRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public UserController(UserServiceImpl userServiceImpl, UserRepository userRepository, FileStore fileStore, AppealCrudRepository appealCrudRepository,
                          ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.userServiceImpl = userServiceImpl;
        this.fileStore = fileStore;
        this.userRepository = userRepository;
        this.appealCrudRepository = appealCrudRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
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

        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            user.setPhoto(filename);
            user.setPhotobase64(Base64.getEncoder().encodeToString(file.getBytes()));
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
        if (user.getPhoto() == null || user.getPhoto().isEmpty() || user.getPhotobase64() == null || user.getPhotobase64().isEmpty()) {
            return new byte[0];
        }

        return Base64.getDecoder().decode(user.getPhotobase64());
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

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    @PostMapping("/push")
    public ResponseEntity push(@RequestBody PushDto params, Authentication authentication){
        int userId = ((JwtUser)authentication.getPrincipal()).getId();
        String result = sendPush(params.getUserId(),userId,params.getMessage(),"admin");
        Map<Object, Object> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/complain")
    public ResponseEntity complain(@RequestParam Integer userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Integer cnt = user.getComplaint();
            if (cnt == null) cnt = 0;
            user.setComplaint(cnt+1);
            userRepository.save(user);
            Map<Object, Object> response = new HashMap<>();
            response.put("result", "OK");
            return ResponseEntity.ok(response);
        } else
            throw new IllegalStateException("User not found");
    }

    public String sendPush(int toUserId, int fromUserId, String message, String categoryName)
    {
        try {
            User toUser = getUserProfileOrThrow(toUserId);
            User fromUser = getUserProfileOrThrow(fromUserId);

            if (toUser.getTokenDevice() == null || toUser.getTokenDevice().isEmpty())
                throw new IllegalStateException("tokenDevice is null");

            final ApnsClient apnsClient = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                    .setClientCredentials(getFileFromResourceAsStream("push_distr.p12"), "1")
                    .build();

            final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
            payloadBuilder.setAlertBody(message);

            if (categoryName.equals("chat")) {
                StringBuilder alertTitle = new StringBuilder();
                if (fromUser.getFirstName() != null && !fromUser.getFirstName().isEmpty())
                    alertTitle.append(fromUser.getFirstName());
                if (fromUser.getLastName() != null && !fromUser.getLastName().isEmpty()) {
                    if (alertTitle.length() > 0) alertTitle.append(" ");
                    alertTitle.append(fromUser.getLastName());
                }
                if (alertTitle.length() == 0)
                    alertTitle.append(fromUser.getEmail());

                payloadBuilder.setAlertTitle(alertTitle.toString());
            }

            payloadBuilder.setBadgeNumber(1);
            payloadBuilder.setCategoryName(categoryName);
            payloadBuilder.setSound("default");

            final String payload = payloadBuilder.build();
            final String token = TokenUtil.sanitizeTokenString(String.format("<%s>",toUser.getTokenDevice()));

            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.ds.Guard", payload);
            final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
            sendNotificationFuture = apnsClient.sendNotification(pushNotification);


            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    sendNotificationFuture.get();

            if (pushNotificationResponse.isAccepted()) {
                System.out.println("Push notification accepted by APNs gateway.");
            } else {
                System.out.println("Notification rejected by the APNs gateway: " +
                        pushNotificationResponse.getRejectionReason());

                pushNotificationResponse.getTokenInvalidationTimestamp().ifPresent(timestamp -> {
                    System.out.println("\t…and the token is invalid as of " + timestamp);
                });

                throw new IllegalStateException("Notification rejected by the APNs gateway");
            }

            return "ok";
        } catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);

            List<Appeal> listAppeal = appealCrudRepository.findAllByClientId(user.getId());
            for (Appeal item: listAppeal) {
                appealCrudRepository.delete(item);
            }

            List<ChatRoom> listChatRoom = chatRoomRepository.findAllByUserId(user.getId());
            for (ChatRoom item: listChatRoom)
            {
                chatRoomRepository.delete(item);
            }

            List<ChatRoom> listChatRoomLawyer = chatRoomRepository.findAllByLawyerId(user.getId());
            for (ChatRoom item: listChatRoomLawyer)
            {
                chatRoomRepository.delete(item);
            }

            List<ChatMessage> listChatMessage = chatMessageRepository.findAllBySenderId(user.getId());
            for (ChatMessage item: listChatMessage)
            {
                chatMessageRepository.delete(item);
            }

            Map<Object, Object> response = new HashMap<>();
            response.put("result", "OK");
            return ResponseEntity.ok(response);
        } else
            throw new IllegalStateException("User not found");
    }
}
