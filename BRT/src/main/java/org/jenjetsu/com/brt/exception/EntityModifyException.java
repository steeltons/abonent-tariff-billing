package org.jenjetsu.com.brt.exception;

public class EntityModifyException extends RuntimeException{

	public EntityModifyException() {
		super();
	}
	
	public EntityModifyException(String message) {
		super(message);
	}
	
	public EntityModifyException(Exception cause) {
		super(cause);
	}
	
	public EntityModifyException(String message, Exception cause) {
		super(message, cause);
	}
}
