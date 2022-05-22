package com.gmn.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * Customized Exception class for handling exception.
 * 
 * @author GMN
 *
 */
@Getter
@AllArgsConstructor
public class TodoException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;

}
