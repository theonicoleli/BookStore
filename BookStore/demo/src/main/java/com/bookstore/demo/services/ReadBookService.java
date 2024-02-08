package com.bookstore.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.ReadBook;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.ReadBookRepository;

@Service
public class ReadBookService {
    
    private ReadBookRepository readBookRepository;
    
    @Autowired
    public ReadBookService(ReadBookRepository readBookRepository) {
    	this.readBookRepository = readBookRepository;
    }
    
    public void addReadBook(User user, Book book) {
        if (user != null && book != null && !readBookRepository.bookReadAlreadyAdded(user.getId(), book.getId())) {
            ReadBook readBook = new ReadBook(user, book);
            user.getReadBooks().add(readBook);
            readBookRepository.save(readBook);
        }
    }

    public void onDeleteUser(User user) {
    	for (ReadBook book: user.getReadBooks()) {
    		book.setUser(null);
    	}
    }
    
    public List<ReadBook> getByUser(User user) {
    	return readBookRepository.findAllByUser(user);
    }
    
    public void deleteReadBook(Long readBookId) {
        try {
            readBookRepository.deleteById(readBookId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir a entrada de ReadBook com ID: " + readBookId, e);
        }
    }

}
