package com.bookstore.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.model.Friendship;
import com.bookstore.demo.model.Notification;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.FriendShipRepository;

@Service
public class FriendShipService {

    @Autowired
    private FriendShipRepository friendshipRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }
    
    public int countUserFriends(User user) {
    	return friendshipRepository.countFriendsForUser(user);
    }
    
    public boolean existsFriendShip(User user1, User user2) {
    	return friendshipRepository.existsByUser1AndUser2(user1, user2);
    }

    public void sendFriendshipRequest(User sender, User receiver) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setMessage("Você recebeu uma solicitação de amizade de " + sender.getName());
        notification.setFriendShipAccept(false);

        notificationService.createNotification(notification);
    }

    public boolean acceptFriendship(Long notificationId, boolean answer) {
        Notification notification = notificationService.getNotificationById(notificationId);
        if (notification != null) {
        	
        	if (answer) {
        		notification.setFriendShipAccept(answer);
        		
                notificationService.updateNotification(notification);

                User sender = notification.getSender();
                User receiver = notification.getReceiver();
                createFriendship(sender, receiver, notificationId);
                
                return true;
        	}
        }
        return false;
    }
    
    public boolean rejectFriendship(Long notificationId) {
        Notification notification = notificationService.getNotificationById(notificationId);
        if (notification != null) {
            notificationService.deleteNotification(notification);
            return true;
        }
        return false;
    }

    private Friendship createFriendship(User user1, User user2, Long notificationId) {
        boolean existingFriendship = friendshipRepository.existsByUser1AndUser2(user1, user2);

        if (existingFriendship) {
        	notificationService.deleteNotification(notificationService.getNotificationById(notificationId));
            return null;
        }

        Friendship friendship = new Friendship();
        friendship.setUser1(user1);
        friendship.setUser2(user2);
        notificationService.deleteNotification(notificationService.getNotificationById(notificationId));
        return friendshipRepository.save(friendship);
    }
    
    public boolean deleteFriendship(User user1, User user2) {
        Optional<Friendship> friendships = friendshipRepository.findFriendship(user1, user2);
        if (!friendships.isEmpty()) {
            Friendship friendshipToDelete = friendships.get();

            if (friendshipToDelete != null) {
                friendshipRepository.delete(friendshipToDelete);
                return true;
            }
        }
        return false;
    }

    public List<Friendship> getFriendshipsByUser(User user) {
        return friendshipRepository.findFriendshipsByUser(user);
    }
    
    public Optional<Long> findFriendshipIdByUsers(User user1, User user2) {
        return friendshipRepository.findFriendshipIdByUser1AndUser2(user1, user2);
    }
    
}
