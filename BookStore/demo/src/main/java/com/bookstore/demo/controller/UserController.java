package com.bookstore.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.demo.services.UserService;

import jakarta.persistence.EntityNotFoundException;

import com.bookstore.demo.exceptions.UserException;
import com.bookstore.demo.model.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

	 private final UserService userService;
	
	 @Autowired
	 public UserController(UserService userService) {
	     this.userService = userService;
	 }
	
	 @GetMapping
	 public List<User> getAllUsers() {
	     return userService.getAllUsers();
	 }
	 
	 @GetMapping("/{userEmail}/{userPassword}")
	 public ResponseEntity<User> getUserByEmailAndPassword(@PathVariable String userEmail, @PathVariable String userPassword) {
	     try {
	         User user = userService.getUserByEmailAndPassword(userEmail, userPassword);
	         return ResponseEntity.ok(user);
	     } catch (EntityNotFoundException e) {
	         return ResponseEntity.notFound().build();
	     } catch (Exception e) {
	         throw new UserException("Erro ao obter o usuário com o email: " + userEmail, e);
	     }
	 }
	
	 @GetMapping("/{userId}")
	 public ResponseEntity<User> getUserById(@PathVariable Long userId) {
	     try {
	         User user = userService.getUserById(userId);
	         return ResponseEntity.ok(user);
	     } catch (EntityNotFoundException e) {
	         return ResponseEntity.notFound().build();
	     } catch (Exception e) {
	         throw new UserException("Erro ao obter o usuário com ID: " + userId, e);
	     }
	 }
	 
	 @GetMapping("/count/{email}")
	 public ResponseEntity<Integer> countUserByEmail(@PathVariable String email) {
		 try {
			 Integer userEmails = userService.countUserByEmail(email);
			 return ResponseEntity.ok(userEmails);
		 } catch (Exception e) {
			 throw new UserException("Nenhum usuário adicionado com o email: " + email);
		 }
	 }
	 
	    @GetMapping("/book/{bookId}")
	    public ResponseEntity<User> getUserByBookId(@PathVariable String bookId) {
	        try {
	            long id = Long.parseLong(bookId);
	            User user = userService.getUserByBookId(id);
	            return ResponseEntity.ok(user);
	        } catch (NumberFormatException e) {
	            return ResponseEntity.badRequest().build();
	        } catch (EntityNotFoundException e) {
	            return ResponseEntity.notFound().build();
	        } catch (NullPointerException e) {
	            return ResponseEntity.notFound().build();
	        } 
	        catch (Exception e) {
	            throw new UserException("Erro ao obter o usuário com o livro: " + bookId, e);
	        }
	    }
	
	 @PostMapping
	 public ResponseEntity<User> addUser(@RequestBody User user) {
	     try {
	    	 if (userService.countUserByEmail(user.getEmail()) > 0) {
	    		 throw new UserException("Email já cadastrado, faça seu login!");
	    	 }
	    	 
	         User addedUser = userService.addUser(user);

	         List<Book> books = addedUser.getBookList();
	         if (books != null && !books.isEmpty()) {
	             for (Book book : books) {
	                 book.setUser(addedUser);
	             }
	         }

	         return ResponseEntity.ok(addedUser);
	     } catch (Exception e) {
	         throw new UserException("Erro ao adicionar um novo usuário", e);
	     }
	 }
	 
	 @PutMapping("/{userId}")
	 public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
	     try {
	         User existingUser = userService.getUserById(userId);
	         
	         existingUser.setName(updatedUser.getName());
	         existingUser.setEmail(updatedUser.getEmail());
	         existingUser.setPassword(updatedUser.getPassword());
	         
	         User savedUser = userService.updateUser(existingUser);

	         return ResponseEntity.ok(savedUser);
	     } catch (Exception e) {
	         throw new UserException("Erro ao atualizar a lista de livros do usuário com ID: " + userId, e);
	     }
	 }
	
	 @DeleteMapping("/{userId}")
	 public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
	     try {
	         userService.deleteUser(userId);
	         return ResponseEntity.noContent().build();
	     } catch (EntityNotFoundException e) {
	         return ResponseEntity.notFound().build();
	     } catch (Exception e) {
	         throw new UserException("Erro ao excluir o usuário com ID: " + userId, e);
	     }
	 }

}
