package com.gmn.todo.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmn.todo.entity.Todo;
import com.gmn.todo.exception.TodoException;
import com.gmn.todo.repository.TodoApiRepository;

/**
 * 
 * The implementation class which handles business logic for todo application.
 * 
 * @author GMN
 *
 */
@Service
public class TodoApiServiceImpl implements TodoApiService {

	public static final String TODO_NOT_PRESENT = "TODO item doesn't exist";

	@Autowired
	TodoApiRepository todoApiRepository;

	/**
	 *
	 * This method returns the list of todo items based on the value that is passed.
	 * if isComplete : true, it will return the todo items marked as complete else
	 * it will return all the todo items that are available irrespective of status.
	 *
	 *
	 * @param isComplete
	 *
	 * @return List of Todo Items.
	 *
	 */
	@Override
	public List<Todo> getAllTodoItems(boolean isComplete) {
		return !isComplete ? (List<Todo>) todoApiRepository.findAll()
				: (List<Todo>) todoApiRepository.findCompletedTodos();

	}

	/**
	 *
	 * This method will add, new todo item to the database , if the passed item is
	 * already available it will not be added again.
	 *
	 * @param todo
	 *
	 * @return Added Todo Item.
	 *
	 */
	@Override
	public Todo addTodoItem(Todo todo) throws TodoException {
		Todo todoPresent = todoApiRepository.findTodoByName(todo.getTodoItem());
		if (Objects.nonNull(todoPresent)) {
			throw new TodoException("TODO item already Exist");
		}

		return todoApiRepository.save(todo);
	}

	/**
	 *
	 * This method will remove/delete the passed todo item from the database
	 *
	 * @param id
	 *
	 */
	@Override
	public void removeTodoItem(long id) throws TodoException {
		Todo todo = todoApiRepository.findById(id).orElseThrow(() -> new TodoException(TODO_NOT_PRESENT));
		todoApiRepository.delete(todo);
	}

	/**
	 *
	 * This method will mark the todo item as complete in database.
	 * 
	 * @param id
	 */
	@Override
	public Todo markTodoComplete(long id) throws TodoException {
		Todo todo = todoApiRepository.findById(id).orElseThrow(() -> new TodoException(TODO_NOT_PRESENT));
		todo.setComplete(true);
		return todoApiRepository.save(todo);

	}

	/**
	 *
	 *
	 * This method will remove/delete all existing todo items available in database.
	 *
	 */
	@Override
	public void clearTodoItems() {
		todoApiRepository.deleteAll();
	}

}
