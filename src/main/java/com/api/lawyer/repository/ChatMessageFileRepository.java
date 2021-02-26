package com.api.lawyer.repository;

import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.ChatMessageFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageFileRepository extends CrudRepository<ChatMessageFile, String> {
    List<ChatMessageFile> findAllByChatMessageId(Integer messageId);
}
