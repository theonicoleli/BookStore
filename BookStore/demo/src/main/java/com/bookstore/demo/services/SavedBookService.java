package com.bookstore.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.SavedBook;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.SavedBookRepository;

@Service
public class SavedBookService {
    
    private SavedBookRepository savedBookRepository;
    private BookService bookService;
    private UserService userService;
    
    @Autowired
    public SavedBookService(SavedBookRepository savedBookRepository, UserService userService, BookService bookService) {
        this.savedBookRepository = savedBookRepository;
        this.userService = userService;
        this.bookService = bookService;
    }
    
    public List<SavedBook> getAllSavedBooks(User user) {
        return savedBookRepository.findByUserId(user.getId());
    }
    
    public boolean isBookSavedByUser(Long userId, Long bookId) {
        Optional<SavedBook> savedBook = savedBookRepository.findByUserIdAndBookId(userId, bookId);
        return savedBook.isPresent();
    }
    
    public void deleteSavedBook(User user, Long bookId) {
        Optional<SavedBook> savedBook = user.getSavedBooks()
                                            .stream()
                                            .filter(saved -> saved.getBook().getId().equals(bookId))
                                            .findFirst();

        savedBook.ifPresentOrElse(
            saved -> {
            	user.getSavedBooks().remove(saved);
                savedBookRepository.deleteById(saved.getId());
                System.out.println("Livro salvo excluído com sucesso.");
            },
            () -> System.out.println("Livro salvo não encontrado para exclusão.")
        );
    }
    
    public SavedBook addSavedBook(User user, Long bookId) {
        SavedBook addBook = new SavedBook();
        Optional<Book> book = bookService.getBookById(bookId);

        if (book.isPresent()) {
            if (userService.getUserById(user.getId()) != null && 
                savedBookRepository.findByUserIdAndBookId(user.getId(), bookId).isEmpty()) {
                
                addBook.setUser(user);
                addBook.setBook(book.get());
                
                return savedBookRepository.save(addBook);
            }
        }
        
        return null;
    }

}
