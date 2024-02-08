package com.bookstore.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.ReadBook;
import com.bookstore.demo.model.User;
import com.bookstore.demo.model.UserReadBookDTO;
import com.bookstore.demo.services.ReadBookService;
import com.bookstore.demo.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/read-books")
public class ReadBookController {

    private ReadBookService readBookService;
    private UserService userService;

    @Autowired
    public ReadBookController(ReadBookService readBookService, UserService userService) {
        this.readBookService = readBookService;
        this.userService = userService;
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReadBookDTO>> getReadBooksByUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            List<ReadBook> readBooks = readBookService.getByUser(user);

            List<UserReadBookDTO> userReadBookDTOList = new ArrayList<>();
            for (ReadBook readBook : readBooks) {
                UserReadBookDTO userReadBookDTO = new UserReadBookDTO();
                userReadBookDTO.setReadBookId(readBook.getId());
                userReadBookDTO.setUserId(user.getId());
                userReadBookDTO.setBookId(readBook.getBook().getId());
                userReadBookDTOList.add(userReadBookDTO);
            }

            return ResponseEntity.ok(userReadBookDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<String> addReadBook(@RequestParam Long userId, @RequestBody Book book) {
        try {
            User user = userService.getUserById(userId);
            readBookService.addReadBook(user, book);
            return ResponseEntity.status(HttpStatus.CREATED).body("Book added to read list successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add book to read list.");
        }
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        try {
        	User user = userService.getUserById(userId);
            readBookService.onDeleteUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user.");
        }
    }
}
