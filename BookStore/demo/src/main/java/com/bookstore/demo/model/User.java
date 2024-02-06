package com.bookstore.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String userName;
	
	private String email;
	
	private String password;
	
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> books;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public List<Book> getBookList() {
	    if (books == null) {
	        books = new ArrayList<>();
	    }
	    return books;
	}

    public void addBook(Book book) {
        getBookList().add(book);
        book.setUser(this);
    }

    public void removeBook(Book book) {
        getBookList().remove(book);
        book.setUser(null);
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setBooks(List<Book> userBooks) {
		this.books = userBooks;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}

