package com.bookstore.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.demo.model.Friendship;
import com.bookstore.demo.model.User;

@Repository
public interface FriendShipRepository extends JpaRepository<Friendship, Long> {
	
    List<Friendship> findByUser1OrUser2(User user1, User user2);
    
    boolean existsByUser1AndUser2(User user1, User user2);
    
}
