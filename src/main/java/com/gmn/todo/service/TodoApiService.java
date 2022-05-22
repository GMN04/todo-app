package com.gmn.todo.service;

import java.util.List;

import com.gmn.todo.entity.Todo;
import com.gmn.todo.exception.TodoException;

/**
 * 
 * Interface for managing todo application methods.
 * 
 * @author GMN
 *
 */
public interface TodoApiService {

	List<Todo> getAllTodoItems(boolean isComplete);

	Todo addTodoItem(Todo todo) throws TodoException;

	void removeTodoItem(long id) throws TodoException;

	Todo markTodoComplete(long id) throws TodoException;

	void clearTodoItems() throws TodoException;
}
