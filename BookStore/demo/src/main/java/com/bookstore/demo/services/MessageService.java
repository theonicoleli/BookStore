package com.bookstore.demo.services;

import com.bookstore.demo.model.Message;
import com.bookstore.demo.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
    
    public List<Message> findAllMessagesByFriendshipId(Long friendshipId) {
        return messageRepository.findAllByFriendshipId(friendshipId);
    }
    
    public void onDeleteFriendShip(Long friendshipId) {
        List<Message> messages = messageRepository.findAllByFriendshipId(friendshipId);
        List<Long> messageIds = messages.stream().map(Message::getId).collect(Collectors.toList());
        messageRepository.deleteAllById(messageIds);
    }

}
