package com.bookstore.demo.exceptions;

public class CommentException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }

}
