package com.bookstore.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.bookstore.demo.enums.BooksTheme;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "book")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private boolean status;
	
	private String imagePath;
	
	private String description;
	
	private BooksTheme theme;
	
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "book")
    private List<ReadBook> readBooks;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Comment> comments;

	public Long getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

    public void setUser(User user) {
    	this.user = user;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

    public String getDescription() {
    	return description;
    }
    
    public String getTheme() {
        return String.valueOf(theme);
    }
    
    public void setTheme(String theme) {
        try {
            this.theme = BooksTheme.valueOf(theme);
        } catch (IllegalArgumentException e) {
            System.out.println("Tema inválido: " + theme);
        }
    }
    
    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void addComment(Comment comment) {
        getComments().add(comment);
        comment.setBook(this);
    }

    public void removeComment(Comment comment) {
        getComments().remove(comment);
        comment.setBook(null);
    }

    @JsonIgnore
	public List<ReadBook> getReadBooks() {
		if (readBooks == null) {
			readBooks = new ArrayList<>();
		}
		return readBooks;
	}

    
}
