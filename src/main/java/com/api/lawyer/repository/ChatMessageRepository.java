package com.api.lawyer.repository;

import com.api.lawyer.dto.ChatMessageDto;
import com.api.lawyer.model.User;
import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.MessageStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, String> {

    Optional<ChatMessage> findById(Integer id);
    List<ChatMessage> findAllByChatId(Integer chatId);

    @Query( value = "select * from chat_message where chat_id = ?1 and coalesce(sender_id,0) != ?2 and coalesce(read,0)=0", nativeQuery = true)
    List<ChatMessage> findNotMyMessagesByChatId(Integer chatId, Integer userId);

    List<ChatMessage> findAllBySenderId(Integer senderId);
}
