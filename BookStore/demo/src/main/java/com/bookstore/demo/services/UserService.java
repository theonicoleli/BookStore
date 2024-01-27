package com.bookstore.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	 @Autowired
	 private UserRepository userRepository;
	
	 public List<User> getAllUsers() {
	     return userRepository.findAll();
	 }
	
	 public User getUserById(Long userId) {
	     return userRepository.findById(userId)
	             .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + userId + " não encontrado."));
	 }
	
	 public User addUser(User user) {
	     return userRepository.save(user);
	 }
	
	 public void deleteUser(Long userId) {
	     if (userRepository.existsById(userId)) {
	         userRepository.deleteById(userId);
	     } else {
	         throw new EntityNotFoundException("Usuário com ID " + userId + " não encontrado. Não foi possível excluir.");
	     }
	 }

	 public User updateUser(User user) {
	    Long userId = user.getId();
	    if (userRepository.existsById(userId)) {
	        return userRepository.save(user);
	    } else {
	        throw new EntityNotFoundException("User with ID " + userId + " not found. Cannot update.");
		}
	}

}
