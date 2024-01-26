package com.bookstore.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.User;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.user = :user")
    List<Book> findAllByUser(User user);
}
