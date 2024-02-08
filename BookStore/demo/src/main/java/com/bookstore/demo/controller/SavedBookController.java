package com.bookstore.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.demo.model.SavedBook;
import com.bookstore.demo.model.User;
import com.bookstore.demo.services.SavedBookService;
import com.bookstore.demo.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saved-books")
public class SavedBookController {

    private SavedBookService savedBookService;
    private UserService userService;

    @Autowired
    public SavedBookController(SavedBookService savedBookService, UserService userService) {
        this.savedBookService = savedBookService;
        this.userService = userService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteSavedBook(@RequestParam Long userId, @RequestBody SavedBook savedBook) {
        try {
        	User user = userService.getUserById(userId);
            savedBookService.deleteSavedBook(user, savedBook);
            return ResponseEntity.status(HttpStatus.OK).body("Saved book deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete saved book.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<SavedBook> addSavedBook(@RequestParam Long userId, @RequestBody SavedBook savedBook) {
        try {
            User user = userService.getUserById(userId);
            SavedBook savedBookAdded = savedBookService.addSavedBook(user, savedBook);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBookAdded);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
