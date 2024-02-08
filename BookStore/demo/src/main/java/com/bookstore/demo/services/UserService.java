package com.bookstore.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.BookRepository;
import com.bookstore.demo.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	 @Autowired
	 private UserRepository userRepository;
	 
	 @Autowired
	 private BookRepository bookRepository;
	
	 public List<User> getAllUsers() {
	     return userRepository.findAll();
	 }
	 
	 public Integer countUserByEmail(String email) {
		 return userRepository.countByEmail(email);
	 }
	
	 public User getUserById(Long userId) {
	     return userRepository.findById(userId)
	             .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + userId + " não encontrado."));
	 }
	 
	 public User getUserByEmailAndPassword(String userEmail, String userPassword) {
	    Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmailPassword(userEmail, userPassword));

	    if (userOptional.isPresent()) {
	        return userOptional.get();
	    } else {
	        throw new EntityNotFoundException("Usuário com email " + userEmail + " e senha fornecida não encontrado.");
	    }
	}
	 
	 public User getUserByUserName(String userName) {
		 return userRepository.findUserByUserName(userName);
	 }
	 
	 public boolean existsUserName(String userName) {
		 if (getUserByUserName(userName) != null) {
			 return true;
		 }
		 return false;
	 }

	 public User addUser(User user) {
	     return userRepository.save(user);
	 }
	
	 public void deleteUser(Long userId) {
	    if (userRepository.existsById(userId)) {
	        User user = getUserById(userId);

	        List<Book> booksToRemove = new ArrayList<>(user.getBookList());

	        for (Book book : booksToRemove) {
	            deleteBookFromUser(userId, book.getId());
	        }

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
	 
	 public void deleteBookFromUser(Long userId, Long bookId) {
	    Optional<User> userOptional = userRepository.findById(userId);
	    
	    if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        
	        Optional<Book> bookOptional = bookRepository.findById(bookId);
	        
	        if (bookOptional.isPresent()) {
	            Book book = bookOptional.get();
	            
	            if (user.getBookList().contains(book)) {
	                book.setUser(null);
	                bookRepository.save(book);
	                
	                user.getBookList().remove(book);
	                userRepository.save(user);
	            }
	        }
	    }
	 }
	 
	 public User getUserByBookId(Long bookId) {
		 Optional<User> userOptional = Optional.of(userRepository.findByBookId(bookId));

		 return userOptional.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	}

}
