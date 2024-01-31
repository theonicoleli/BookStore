package com.bookstore.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstore.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    User findById(long userId);
    
    @Query("SELECT u FROM User u WHERE u.email = :userEmail AND u.password = :userPassword")
    User findByEmailPassword(@Param("userEmail") String email, @Param("userPassword") String password);

}
