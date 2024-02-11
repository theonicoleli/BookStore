package com.bookstore.demo.services;

import com.bookstore.demo.enums.BooksTheme;
import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.BookRequest;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.BookRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final ReadBookService readBookService;

    @Autowired
    public BookService(BookRepository bookRepository, UserService userService, ReadBookService readBookService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.readBookService = readBookService;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public List<Long> getAllBooksId() {
    	return bookRepository.findAllBookIds();
    }
    
    public List<Book> getAllBooksByUserId(User user){
    	return bookRepository.findAllByUser(user);
    }
    
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
    
    public List<Book> getAllBooksByTheme(String theme) {
    	return bookRepository.findAllByTheme(BooksTheme.valueOf(theme));
    }
    
    public List<Book> getAllBooksByStatus(boolean status) {
    	return bookRepository.findAllByStatus(status);
    }
    
    public Optional<Book> getBookById(long bookId) {
    	return bookRepository.findById(bookId);
    }

    public Book updateBook(Long bookId, BookRequest bookRequest) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent() && bookRequest != null) {
            Book existingBook = optionalBook.get();
            existingBook.setName(bookRequest.getName());
            existingBook.setStatus(bookRequest.isStatus());
            existingBook.setImagePath(bookRequest.getImagePath());
            existingBook.setDescription(bookRequest.getDescription());
            
            Long userId = bookRequest.getUserId();
            if (userId != null) {
                User user = userService.getUserById(userId);
                existingBook.setUser(user);
                readBookService.addReadBook(user, existingBook);
            }

            return bookRepository.save(existingBook);
        } else {
            throw new EntityNotFoundException("Livro com ID " + bookId + " não encontrado ou solicitação inválida.");
        }
    }

    public void deleteBook(Long bookId) {	
        bookRepository.deleteById(bookId);
    }
    
    public Book updateStatus(Long bookId, long userId, boolean newStatus) {
        User user = userService.getUserById(userId);
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();

            if (!newStatus) {
                existingBook.setUser(null);
            } else {
                existingBook.setUser(user);
                readBookService.addReadBook(user, existingBook);
            }

            existingBook.setStatus(!newStatus);

            return bookRepository.save(existingBook);
        } else {
            throw new EntityNotFoundException("Livro com ID " + bookId + " não encontrado.");
        }
    }

    
    public void updateAllUserBookStatus(Long userId) {
    	User user = userService.getUserById(userId);
    	
    	for (Book book: bookRepository.findAllByUser(user)) {
    		updateStatus(book.getId(), user.getId(), false);
    	}
    }

}
