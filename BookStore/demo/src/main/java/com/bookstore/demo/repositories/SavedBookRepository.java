package com.bookstore.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.demo.model.SavedBook;

@Repository
public interface SavedBookRepository extends JpaRepository<SavedBook, Long> {
	
	Optional<SavedBook> findByUserIdAndBookId(Long userId, Long bookId);
	
	void deleteByUserIdAndBookId(Long userId, Long bookId);
	
	List<SavedBook> findByUserId(Long userId);
}
