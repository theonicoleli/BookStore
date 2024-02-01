package com.bookstore.demo.controller;

import com.bookstore.demo.exceptions.BookException;
import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.BookRequest;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.BookRepository;
import com.bookstore.demo.repositories.UserRepository;
import com.bookstore.demo.services.BookService;
import com.bookstore.demo.services.UserService;
import com.bookstore.demo.util.UploadUtil;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
		this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        try {
            return bookService.getAllBooks();
        } catch (Exception e) {
            throw new BookException("Erro ao obter todos os livros", e);
        }
    }
    
    @GetMapping("/theme/{booksTheme}")
    public ResponseEntity<List<Book>> getAllBooksByTheme(@PathVariable String booksTheme) {
        try {
            List<Book> books = bookService.getAllBooksByTheme(booksTheme);

            if (books.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/status/{bookStatus}")
    public ResponseEntity<List<Book>> getAllBooksByStatus(@PathVariable boolean bookStatus) {
        try {
            List<Book> books = bookService.getAllBooksByStatus(bookStatus);

            if (books.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/{bookId}")
    public ResponseEntity<Optional<Book>> getBookById(@PathVariable long bookId) {
        try {
            Optional<Book> book = bookService.getBookById(bookId);

            if (book.isPresent()) {
                return ResponseEntity.ok(book);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PostMapping
    public ResponseEntity<Book> addBook(@ModelAttribute BookRequest bookRequest, @RequestParam("image") MultipartFile image) {
        try {
            Book newBook = new Book();
            newBook.setName(bookRequest.getName());
            newBook.setStatus(bookRequest.isStatus());
            newBook.setDescription(bookRequest.getDescription());
            newBook.setTheme(bookRequest.getTheme());

            if (bookRequest.getUserId() != null) {
                User user = userRepository.findById(bookRequest.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + bookRequest.getUserId()));

                newBook.setUser(user);

                if (user.getBookList() == null) {
                    user.setBooks(new ArrayList<>());
                }
                user.getBookList().add(newBook);

                userRepository.save(user);
            }

            if (UploadUtil.fazerUploadImagem(image)) {
                newBook.setImagePath(image.getOriginalFilename());
            } else {
                throw new Exception("Falha ao fazer upload da imagem");
            }

            bookRepository.save(newBook);

            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@PathVariable long bookId, BookRequest bookRequest, MultipartFile image) {
        try {
            Optional<Book> optionalBook = bookRepository.findById(bookId);

            if (optionalBook.isPresent()) {
                Book existingBook = optionalBook.get();

                existingBook.setName(bookRequest.getName());
                existingBook.setStatus(bookRequest.isStatus());
                existingBook.setDescription(bookRequest.getDescription());
                existingBook.setTheme(bookRequest.getTheme());

                if (bookRequest.getUserId() != null) {
                    User user = userRepository.findById(bookRequest.getUserId())
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + bookRequest.getUserId()));

                    existingBook.setUser(user);

                    if (user.getBookList() == null) {
                        user.setBooks(new ArrayList<>());
                    }
                    user.getBookList().add(existingBook);

                    userRepository.save(user);
                } else {
                    if (existingBook.getUser() != null) {
                        userService.deleteBookFromUser(existingBook.getUser().getId(), bookId);
                    }

                    existingBook.setUser(null);
                }

                if (image != null && !image.isEmpty()) {
                    if (UploadUtil.fazerUploadImagem(image)) {
                        if (existingBook.getImagePath() != null) {
                            UploadUtil.deleteImage(existingBook.getImagePath());
                        }
                        existingBook.setImagePath(image.getOriginalFilename());
                    } else {
                        throw new Exception("Falha ao fazer upload da nova imagem");
                    }
                } else {
                    existingBook.setImagePath(null);
                }

                return bookRepository.save(existingBook);
            } else {
                throw new EntityNotFoundException("Livro com ID " + bookId + " não encontrado.");
            }
        } catch (Exception e) {
            throw new BookException("Erro ao atualizar o livro com ID: " + bookId, e);
        }
    }


    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        try {
        	bookService.deleteBook(bookId);
        } catch (Exception e) {
            throw new BookException("Erro ao excluir o livro com ID: " + bookId, e);
        }
    }
    
    @PatchMapping("/{bookId}/status")
    public ResponseEntity<Book> updateStatus(@PathVariable Long bookId, @RequestParam boolean newStatus) {
        try {
            Book updatedBook = bookService.updateStatus(bookId, newStatus);
            return ResponseEntity.ok(updatedBook);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new BookException("Erro ao atualizar o status do livro com ID: " + bookId, e);
        }
    }
    
}
