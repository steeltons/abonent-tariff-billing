package org.jenjetsu.com.brt.exception;

public class EntityDeleteException extends RuntimeException{

	public EntityDeleteException() {
		super();
	}
	
	public EntityDeleteException(String message) {
		super(message);
	}
	
	public EntityDeleteException(Exception cause) {
		super(cause);
	}
	
	public EntityDeleteException(String message, Exception cause) {
		super(message, cause);
	}
}
