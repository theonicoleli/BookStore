package com.bookstore.demo.controller;

import com.bookstore.demo.exceptions.CommentException;
import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.Comment;
import com.bookstore.demo.model.CommentRequest;
import com.bookstore.demo.model.User;
import com.bookstore.demo.services.BookService;
import com.bookstore.demo.services.CommentService;
import com.bookstore.demo.services.UserService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, BookService bookService) {
        this.commentService = commentService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Comment>> getAllBookComments(@PathVariable Long bookId) {
        try {
            List<Comment> bookComments = commentService.getAllBookComments(bookId);
            return ResponseEntity.ok(bookComments);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getAllUserComments(@PathVariable Long userId) {
        try {
            List<Comment> userComments = commentService.getAllUserComments(userId);
            return ResponseEntity.ok(userComments);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<List<Comment>> getAllUserCommentsForBook(@PathVariable Long userId, @PathVariable Long bookId) {
        try {
            List<Comment> userCommentsForBook = commentService.getAllUserCommentsForBook(userId, bookId);
            return ResponseEntity.ok(userCommentsForBook);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
        Optional<Comment> comment = commentService.getCommentById(commentId);
        return comment.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/infouser/{commentId}")
    public ResponseEntity<User> getUserByCommentId(@PathVariable Long commentId) {
        Optional<User> user = commentService.getUserByCommentId(commentId);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentRequest commentRequest) {
        try {
            if (commentRequest.getText() != null && commentRequest.getUserId() != null && commentRequest.getBookId() != null) {
                Comment comment = new Comment();
                comment.setText(commentRequest.getText());

                User user = userService.getUserById(commentRequest.getUserId());
                Optional<Book> bookOptional = bookService.getBookById(commentRequest.getBookId());

                if (bookOptional.isPresent()) {
                    comment.setUser(user);
                    comment.setBook(bookOptional.get());

                    Comment addedComment = commentService.addComment(comment);
                    return ResponseEntity.status(HttpStatus.CREATED).body(addedComment);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/reply/{parentId}")
    public ResponseEntity<Comment> addCommentToComment(@PathVariable Long parentId, @RequestBody CommentRequest newComment) {
        try {
            Optional<Comment> savedComment = commentService.addCommentToComment(parentId, newComment);
            return savedComment.map(comment -> new ResponseEntity<>(comment, HttpStatus.CREATED))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (CommentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment commentRequest) {
        try {
            Comment updatedComment = commentService.updateComment(commentId, commentRequest);
            return ResponseEntity.ok().body(updatedComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
