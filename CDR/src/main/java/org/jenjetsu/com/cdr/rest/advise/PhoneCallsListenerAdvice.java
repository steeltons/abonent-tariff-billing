package org.jenjetsu.com.cdr.rest.advise;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class PhoneCallsListenerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("placeholder");

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = "Resource not found.\n" +
                "Exist resources:\n"+
                "GET http://localhost:8100/calls/generate-numbers?count=*COUNT OF NUMBERS*\n"+
                "GET http://localhost:8100/calls/get-calls?command=*YOUR COMMAND*\n" +
                "POST http://localhost:8100/calls/generate-calls-from BODY: {\"phoneNumbers\" : [*NUMBERS*]}";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
