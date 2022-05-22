package com.gmn.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gmn.todo.entity.Todo;

/**
 * 
 * The repository class for handling database response.
 * 
 * @author GMN
 *
 */
@Repository
public interface TodoApiRepository extends CrudRepository<Todo, Long> {

	/**
	 * 
	 * This method returns list of todo items that are marked as completed.
	 * 
	 * @return List of Todo Items.
	 */
	@Query("SELECT t FROM Todo t WHERE t.isComplete = true")
	List<Todo> findCompletedTodos();

	/**
	 * 
	 * This method returns the Todo data based on item name.
	 * 
	 * @param todoItem
	 * 
	 * @return Todo Item.
	 */
	@Query("SELECT t FROM Todo t WHERE t.todoItem = ?1")
	Todo findTodoByName(String todoItem);

}
