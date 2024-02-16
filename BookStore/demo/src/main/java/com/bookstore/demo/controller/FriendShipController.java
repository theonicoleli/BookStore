package com.bookstore.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.demo.model.AcceptanceResponse;
import com.bookstore.demo.model.FriendShipRequest;
import com.bookstore.demo.model.Friendship;
import com.bookstore.demo.model.FriendshipDTO;
import com.bookstore.demo.model.User;
import com.bookstore.demo.services.FriendShipService;
import com.bookstore.demo.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/friendships")
public class FriendShipController {

    @Autowired
    private FriendShipService friendshipService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<FriendshipDTO>> getAllFriendships() {
        try {
            List<Friendship> friendships = friendshipService.getAllFriendships();
            List<FriendshipDTO> friendshipDTOs = friendships.stream()
                    .map(friendship -> new FriendshipDTO(friendship.getId(), friendship.getUser1().getId(), friendship.getUser2().getId()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(friendshipDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/existsFriendShip/{user1Id}/{user2Id}")
    public boolean existsFriendShip(@PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
    	try {
    		User user1 = userService.getUserById(user1Id);
    		User user2 = userService.getUserById(user2Id);
    		
    		if (user1 == null || user2 == null) {
    			return false;
    		}
    		
    		return friendshipService.existsFriendShip(user1, user2);
    	} catch (Exception e) {
    		return false;
    	}
    }
    
    @GetMapping("/countUserFriends/{userId}")
    public Integer countUserFriends(@PathVariable("userId") Long userId) {
    	try {
        	User user = userService.getUserById(userId);
        	
        	if (user == null) {
        		return 0;
        	}
        	return friendshipService.countUserFriends(user);
    	} catch (Exception e) {
    		return 0;
    	}
    }

    @PostMapping("/request")
    public ResponseEntity<String> sendFriendshipRequest(@RequestBody FriendShipRequest friendshipRequest) {
        try {
            User sender = userService.getUserById(friendshipRequest.getSenderId());
            User receiver = userService.getUserById(friendshipRequest.getReceiverId());

            friendshipService.sendFriendshipRequest(sender, receiver);
            return new ResponseEntity<>("Friendship request sent successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accept/{notificationId}")
    public ResponseEntity<String> acceptFriendship(@PathVariable Long notificationId, @RequestBody AcceptanceResponse acceptanceResponse) {
        try {
            boolean answer = acceptanceResponse.isAcceptance();

            if (friendshipService.acceptFriendship(notificationId, answer)) {
                return new ResponseEntity<>("Friendship accepted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to accept friendship", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> rejectFriendship(@PathVariable Long id) {
        try {
            if (friendshipService.rejectFriendship(id)) {
                return new ResponseEntity<>("Friendship request rejected successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Notification not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Long>> getFriendshipsByUser(@PathVariable Long userId) {
        try {
            User user = new User();
            user.setId(userId);
            List<Friendship> friendships = friendshipService.getFriendshipsByUser(user);
            List<Long> friendIds = friendships.stream()
                    .map(friendship -> {
                        if (friendship.getUser1().getId().equals(userId)) {
                            return friendship.getUser2().getId();
                        } else {
                            return friendship.getUser1().getId();
                        }
                    })
                    .collect(Collectors.toList());
            return new ResponseEntity<>(friendIds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/deleteFriendShip/{userId1}/{userId2}")
    public ResponseEntity<String> deleteFriendship(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            User user1 = userService.getUserById(userId1);
            User user2 = userService.getUserById(userId2);
            
            if (friendshipService.existsFriendShip(user1, user2)) {
            	
                boolean deleted = friendshipService.deleteFriendship(user1, user2);

                if (deleted) {
                    return ResponseEntity.ok("Friendship deleted successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friendship not found");
                }
            } else {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friendship not found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete friendship");
        }
    }

}