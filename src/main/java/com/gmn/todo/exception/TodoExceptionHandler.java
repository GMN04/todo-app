package com.gmn.todo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * Exception handler class for handling exception and managing api response in
 * case of errors.
 * 
 * @author GMN
 *
 */
@ControllerAdvice
public class TodoExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { TodoException.class })
	protected ResponseEntity<Error> badRequest(TodoException todoException, WebRequest request) {
		String errorResponse = todoException.getMessage();
		Error error = new Error();
		error.setMessage(errorResponse);
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Error> internalServerError(RuntimeException ex, WebRequest request) {
		String errorResponse = "Oops ... We're sorry, our system ran in to some issues";
		Error error = new Error();
		error.setMessage(errorResponse);
		return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	}

}