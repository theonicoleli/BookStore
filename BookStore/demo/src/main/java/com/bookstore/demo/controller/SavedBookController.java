package com.bookstore.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.demo.model.SavedBook;
import com.bookstore.demo.model.User;
import com.bookstore.demo.model.UserReadBookDTO;
import com.bookstore.demo.services.SavedBookService;
import com.bookstore.demo.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/saved-books")
public class SavedBookController {

    private SavedBookService savedBookService;
    private UserService userService;

    @Autowired
    public SavedBookController(SavedBookService savedBookService, UserService userService) {
        this.savedBookService = savedBookService;
        this.userService = userService;
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReadBookDTO>> getSavedBooksByUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            List<SavedBook> savedBooks = savedBookService.getAllSavedBooks(user);
            List<UserReadBookDTO> savedBookDTOList = new ArrayList<>();
            
            for (SavedBook savedBook : savedBooks) {
            	UserReadBookDTO savedBookDTO = new UserReadBookDTO();
                savedBookDTO.setReadBookId(savedBook.getId());
                savedBookDTO.setUserId(user.getId());
                savedBookDTO.setBookId(savedBook.getBook().getId());
                savedBookDTOList.add(savedBookDTO);
            }

            return ResponseEntity.ok(savedBookDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteSavedBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            User user = userService.getUserById(userId);
            savedBookService.deleteSavedBook(user, bookId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Saved book deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Failed to delete saved book."));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<SavedBook> addSavedBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            User user = userService.getUserById(userId);
            SavedBook savedBookAdded = savedBookService.addSavedBook(user, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBookAdded);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
