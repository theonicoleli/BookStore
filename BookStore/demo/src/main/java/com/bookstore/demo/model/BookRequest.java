package com.bookstore.demo.model;

import org.springframework.web.multipart.MultipartFile;

import com.bookstore.demo.enums.BooksTheme;

public class BookRequest {
	
    private String name;
    private boolean status;
    private MultipartFile imagePath;
    private Long userId;
    private String description;
    private BooksTheme theme;
    
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
	public MultipartFile getImagePath() {
		return imagePath;
	}
	public void setImagePath(MultipartFile imagePath) {
		this.imagePath = imagePath;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setTheme(String theme) {
		this.theme = BooksTheme.valueOf(theme);
	}
	public String getTheme() {
		return String.valueOf(theme);
	}
    
}
