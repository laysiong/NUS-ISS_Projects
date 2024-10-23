package com.example.demo.exception;

public class DuplicateAuthException extends DuplicateTypeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4143517220683180947L;

	public DuplicateAuthException(String message) {
        super(message);
    }
}