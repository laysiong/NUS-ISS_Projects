package com.example.demo.exception;

public class DuplicateTypeException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7810378920007387932L;

	public DuplicateTypeException(String message) {
        super(message);
    }
}
