package com.bookstore.demo.controller;

import com.bookstore.demo.exceptions.BookException;
import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.BookRequest;
import com.bookstore.demo.model.ReadBook;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.BookRepository;
import com.bookstore.demo.repositories.UserRepository;
import com.bookstore.demo.services.BookService;
import com.bookstore.demo.services.ReadBookService;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final ReadBookService readBookService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
    public BookController(BookService bookService, BookRepository bookRepository, ReadBookService readBookService) {
        this.bookService = bookService;
		this.bookRepository = bookRepository;
		this.readBookService = readBookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        try {
            return bookService.getAllBooks();
        } catch (Exception e) {
            throw new BookException("Erro ao obter todos os livros", e);
        }
    }
    
    @GetMapping("/id-book")
    public List<Long> getAllBooksId() {
        try {
            return bookService.getAllBooksId();
        } catch (Exception e) {
            throw new BookException("Erro ao obter todos os livros", e);
        }
    }
    
    @GetMapping("/user/{userId}")
    public List<Book> getAllBooksByUserId(@PathVariable long userId) {
    	try {
    		User user = userService.getUserById(userId);
    		return bookService.getAllBooksByUserId(user);
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
            
            if (UploadUtil.fazerUploadImagem(image)) {
            	String imagePath = "assets/img/" + image.getOriginalFilename();
                newBook.setImagePath(imagePath);
            } else {
                throw new Exception("Falha ao fazer upload da imagem");
            }

            if (bookRequest.getUserId() != null) {
                User user = userRepository.findById(bookRequest.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + bookRequest.getUserId()));

                newBook.setUser(user);

                if (user.getBookList() == null) {
                    user.setBooks(new ArrayList<>());
                }
                user.getBookList().add(newBook);
                readBookService.addReadBook(user, newBook);

                userRepository.save(user);
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

                if (image != null && !image.isEmpty()) {
                    if (UploadUtil.fazerUploadImagem(image)) {
                        if (existingBook.getImagePath() != null) {
                            UploadUtil.deleteImage(existingBook.getImagePath());
                        }
                        String imagePath = "assets/img/" + image.getOriginalFilename();
                        existingBook.setImagePath(imagePath);
                    } else {
                        throw new Exception("Falha ao fazer upload da nova imagem");
                    }
                }
                
                if (bookRequest.getUserId() != null) {
                    User user = userRepository.findById(bookRequest.getUserId())
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + bookRequest.getUserId()));

                    existingBook.setUser(user);

                    if (user.getBookList() == null) {
                        user.setBooks(new ArrayList<>());
                    }
                    user.getBookList().add(existingBook);
                    readBookService.addReadBook(user, existingBook);

                    userRepository.save(user);
                } else {
                    if (existingBook.getUser() != null) {
                        userService.deleteBookFromUser(existingBook.getUser().getId(), bookId);
                    }

                    existingBook.setUser(null);
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
            Book bookToDelete = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Livro com ID " + bookId + " não encontrado."));

            List<ReadBook> readBooks = bookToDelete.getReadBooks();
            if (readBooks != null) {
                readBooks.forEach(readBook -> readBookService.deleteReadBook(readBook.getId()));
            }

            bookService.deleteBook(bookId);
        } catch (Exception e) {
            throw new BookException("Erro ao excluir o livro com ID: " + bookId, e);
        }
    }

    
    @PatchMapping("/status/{bookId}/{userId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long bookId, @PathVariable Long userId, @RequestParam boolean newStatus) {
        try {
            Book updatedBook = bookService.updateStatus(bookId, userId, newStatus);
            return ResponseEntity.ok(updatedBook);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new BookException("Erro ao atualizar o status do livro com ID: " + bookId, e);
        }
    }
    
}
