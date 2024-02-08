package com.bookstore.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookstore.demo.model.ReadBook;
import com.bookstore.demo.model.User;

@Repository
public interface ReadBookRepository extends JpaRepository<ReadBook, Long> {

	@Query("SELECT CASE WHEN COUNT(rd) > 0 THEN TRUE ELSE FALSE END FROM ReadBook rd WHERE rd.user.id = :userId AND rd.book.id = :bookId")
	boolean bookReadAlreadyAdded(Long userId, Long bookId);
	
	List<ReadBook> findAllByUser(User user);
}
