package com.bookstore.demo.exceptions;

public class BookException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookException(String message) {
        super(message);
    }

    public BookException(String message, Throwable cause) {
        super(message, cause);
    }
}
