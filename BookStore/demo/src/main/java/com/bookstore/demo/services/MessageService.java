package com.bookstore.demo.services;

import com.bookstore.demo.model.Message;
import com.bookstore.demo.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
