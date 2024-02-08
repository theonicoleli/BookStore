package com.bookstore.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.demo.model.SavedBook;

@Repository
public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {
	
}
