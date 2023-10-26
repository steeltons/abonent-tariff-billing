package org.jenjetsu.com.brt.exception;

public class EntityCreateException extends RuntimeException {

	public EntityCreateException() {
		super();
	}
	
	public EntityCreateException(String message) {
		super(message);
	}
	
	public EntityCreateException(Exception cause) {
		super(cause);
	}
	
	public EntityCreateException(String message, Exception cause) {
		super(message, cause);
	}
}
