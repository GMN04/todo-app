package com.gmn.todo.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gmn.todo.entity.Todo;
import com.gmn.todo.exception.TodoException;
import com.gmn.todo.service.TodoApiService;

/**
 * 
 * Controller Class for managing end points for Todo Application.
 * 
 * @author GMN
 *
 */
@RestController
@RequestMapping("/todo")
public class TodoApiController {

	private static final Logger logger = LoggerFactory.getLogger(TodoApiController.class);

	@Autowired
	private TodoApiService todoApiService;

	@GetMapping("/")
	public ResponseEntity<List<Todo>> getAllTodoItems(@RequestParam(defaultValue = "false") boolean complete)
			throws TodoException {
		logger.info("Inside getAllTodoItems Controller");
		List<Todo> todoList = todoApiService.getAllTodoItems(complete);
		logger.info("Exiting getAllTodoItems Controller ::  " + todoList);
		return new ResponseEntity<>(todoList, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Todo> addTodoItem(@RequestBody @Valid Todo todo) throws TodoException {
		logger.info("Inside addTodoItem Controller :: Request Data -   " + todo);
		Todo todoResp = todoApiService.addTodoItem(todo);
		logger.info("Exiting addTodoItem Controller ::  " + todoResp);
		return new ResponseEntity<>(todoResp, HttpStatus.CREATED);
	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<?> removeTodoItem(@PathVariable("id") long id) throws TodoException {
		logger.info("Inside removeTodoItem Controller :: Request Data -   " + id);
		todoApiService.removeTodoItem(id);
		logger.info("Exiting removeTodoItem Controller");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/complete/{id}")
	public ResponseEntity<Todo> markTodoComplete(@PathVariable("id") long id) throws TodoException {
		logger.info("Inside markTodoComplete Controller :: Request Data -   " + id);
		Todo todoResp = todoApiService.markTodoComplete(id);
		logger.info("Exiting markTodoComplete Controller ::  " + todoResp);
		return new ResponseEntity<>(todoResp, HttpStatus.OK);
	}

	@DeleteMapping("/clear")
	public ResponseEntity<?> clearTodoItems() throws TodoException {
		logger.info("Inside clearTodoItems Controller");
		todoApiService.clearTodoItems();
		logger.info("Exiting clearTodoItems Controller");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
