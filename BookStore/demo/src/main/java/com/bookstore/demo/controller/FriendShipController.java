package com.bookstore.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.Optional;
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
    public ResponseEntity<Boolean> existsFriendShip(@PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        try {
            User user1 = userService.getUserById(user1Id);
            User user2 = userService.getUserById(user2Id);

            if (user1 == null || user2 == null) {
                return ResponseEntity.notFound().build();
            }

            boolean exists = friendshipService.existsFriendShip(user1, user2);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/countUserFriends/{userId}")
    public ResponseEntity<Integer> countUserFriends(@PathVariable("userId") Long userId) {
        try {
            User user = userService.getUserById(userId);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            int count = friendshipService.countUserFriends(user);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/friendshipId/{username1}/{username2}")
    public ResponseEntity<Long> getFriendshipId(@PathVariable String username1, @PathVariable String username2) {
        User user1 = userService.getUserByUserName(username1);
        User user2 = userService.getUserByUserName(username2);

        if (user1 == null || user2 == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Long> friendshipId = friendshipService.findFriendshipIdByUsers(user1, user2);

        if (friendshipId.isPresent()) {
            return ResponseEntity.ok(friendshipId.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/request")
    public ResponseEntity<Map<String, String>> sendFriendshipRequest(@RequestBody FriendShipRequest friendshipRequest) {
        try {
            User sender = userService.getUserById(friendshipRequest.getSenderId());
            User receiver = userService.getUserById(friendshipRequest.getReceiverId());

            friendshipService.sendFriendshipRequest(sender, receiver);
            return ResponseEntity.ok(Collections.singletonMap("message", "Friendship request sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Failed to send friendship request"));
        }
    }

    @PostMapping("/accept/{notificationId}")
    public ResponseEntity<Map<String, String>> acceptFriendship(@PathVariable Long notificationId, @RequestBody AcceptanceResponse acceptanceResponse) {
        try {
            boolean answer = acceptanceResponse.isAcceptance();

            if (friendshipService.acceptFriendship(notificationId, answer)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Friendship accepted successfully");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> rejectFriendship(@PathVariable Long id) {
        try {
            if (friendshipService.rejectFriendship(id)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Friendship request rejected successfully");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<Map<String, String>> deleteFriendship(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            User user1 = userService.getUserById(userId1);
            User user2 = userService.getUserById(userId2);
            
            if (friendshipService.existsFriendShip(user1, user2)) {
            	
                boolean deleted = friendshipService.deleteFriendship(user1, user2);

                if (deleted) {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Friendship deleted successfully");
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Friendship not found"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Friendship not found"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Failed to delete friendship"));
        }
    }
}