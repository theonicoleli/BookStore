package com.bookstore.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookstore.demo.model.Friendship;
import com.bookstore.demo.model.User;

@Repository
public interface FriendShipRepository extends JpaRepository<Friendship, Long> {
	
	@Query("SELECT f FROM Friendship f WHERE f.user1 = :user OR f.user2 = :user")
	List<Friendship> findFriendshipsByUser(User user);
    
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friendship f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
    boolean existsByUser1AndUser2(User user1, User user2);
    
    @Query("SELECT COUNT(f) FROM Friendship f WHERE f.user1 = :user OR f.user2 = :user")
    int countFriendsForUser(User user);
    
    @Query("SELECT f FROM Friendship f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
    Optional<Friendship> findFriendship(User user1, User user2);
    
    @Query("SELECT f.id FROM Friendship f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
	Optional<Long> findFriendshipIdByUser1AndUser2(User user1, User user2);
    
}
