package com.api.lawyer.repository;

import com.api.lawyer.dto.ChatMessageDto;
import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.MessageStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, String> {

    Optional<ChatMessage> findById(Integer id);
    List<ChatMessage> findAllByChatId(Integer chatId);
}
