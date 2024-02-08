package com.bookstore.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.model.SavedBook;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.SavedBookRepository;

@Service
public class SavedBookService {
    
    private SavedBookRepository savedBookRepository;
    
    @Autowired
    public SavedBookService(SavedBookRepository savedBookRepository) {
        this.savedBookRepository = savedBookRepository;
    }
    
    public void deleteSavedBook(User user, SavedBook savedBook) {
    	if (user.getSavedBooks().contains(savedBook)) {
            savedBookRepository.delete(savedBook);
    	}
    }
    
    public SavedBook addSavedBook(User user, SavedBook savedBook) {
        savedBook.setUser(user);
        return savedBookRepository.save(savedBook);
    }
}
