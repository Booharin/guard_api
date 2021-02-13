package com.api.lawyer.repository;

import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.ChatRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {
    List<ChatRoom> findAllByUserId(Integer userId);
    List<ChatRoom> findAllByLawyerId(Integer lawyerId);
    Optional<ChatRoom> findFirstById(Integer id);
    //ChatMessage deleteAllByUserIdAndLa(Integer userId, Integer lawyerId);
}
