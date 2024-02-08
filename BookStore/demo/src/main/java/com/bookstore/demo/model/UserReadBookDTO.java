package com.bookstore.demo.model;

public class UserReadBookDTO {

	private Long readBookId;
    private Long userId;
    private Long bookId;
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	public Long getReadBookId() {
		return readBookId;
	}
	public void setReadBookId(Long readBookId) {
		this.readBookId = readBookId;
	}
	
}
