package com.bookstore.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.demo.services.BookService;
import com.bookstore.demo.services.CommentService;
import com.bookstore.demo.services.ReadBookService;
import com.bookstore.demo.services.UserService;
import com.bookstore.demo.util.UploadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

import com.bookstore.demo.exceptions.UserException;
import com.bookstore.demo.model.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

	 private final UserService userService;
	 private final CommentService commentService;
	 private final BookService bookService;
	 private final ReadBookService readBookService;
	
	 @Autowired
	 public UserController(UserService userService, CommentService commentService, BookService bookService, ReadBookService readBookService) {
	     this.userService = userService;
	     this.commentService = commentService;
	     this.bookService = bookService;
	     this.readBookService = readBookService;
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
	 
	 @GetMapping("/profile/{userName}")
	 public ResponseEntity<User> getUserByUserName(@PathVariable String userName) {
	     try {
	         User user = userService.getUserByUserName(userName);
	         return ResponseEntity.ok(user);
	     } catch (EntityNotFoundException e) {
	         return ResponseEntity.notFound().build();
	     } catch (Exception e) {
	         throw new UserException("Erro ao obter o usuário com o userName: " + userName, e);
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
    public ResponseEntity<User> addUser(@ModelAttribute User user, @RequestParam("image") MultipartFile image) {
        try {
            if (userService.countUserByEmail(user.getEmail()) > 0) {
                throw new UserException("Email já cadastrado, faça seu login!");
            }
            
            if (userService.existsUserName(user.getUserName())) {
                throw new UserException("UserName já cadastrado, tente outro!");
            }
            
            if (UploadUtil.fazerUploadImagem(image)) {
            	String imagePath = "assets/img/" + image.getOriginalFilename();
                user.setImagePath(imagePath);
            } else {
                throw new Exception("Falha ao fazer upload da imagem");
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
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestParam("user") String userJson, @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            User existingUser = userService.getUserById(userId);
            
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);
            
            if (user != null) {
                existingUser.setName(user.getName());
                existingUser.setUserName(user.getUserName());
                existingUser.setEmail(user.getEmail());
                existingUser.setPassword(user.getPassword());
                
                if (image != null && !image.isEmpty()) {
                    if (UploadUtil.fazerUploadImagem(image)) {
                        if (existingUser.getImagePath() != null) {
                            UploadUtil.deleteImage(existingUser.getImagePath());
                        }
                        String imagePath = "assets/img/" + image.getOriginalFilename();
                        existingUser.setImagePath(imagePath);
                    } else {
                        throw new Exception("Falha ao fazer upload da nova imagem");
                    }
                }
            }
            
            User savedUser = userService.updateUser(existingUser);

            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            throw new UserException("Erro ao atualizar a lista de livros do usuário com ID: " + userId, e);
        }
    }
	
	 @DeleteMapping("/{userId}")
	 public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
	     try {
	    	 User user = userService.getUserById(userId);
	    	 readBookService.onDeleteUser(user);
	    	 
	    	 bookService.updateAllUserBookStatus(userId);
	    	 commentService.deleteCommentsByUserId(userId);
	         userService.deleteUser(userId);
	         return ResponseEntity.noContent().build();
	     } catch (EntityNotFoundException e) {
	         return ResponseEntity.notFound().build();
	     } catch (Exception e) {
	         throw new UserException("Erro ao excluir o usuário com ID: " + userId, e);
	     }
	 }

}
