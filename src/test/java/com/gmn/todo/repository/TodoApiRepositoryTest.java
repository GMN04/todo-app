package com.gmn.todo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.gmn.todo.entity.Todo;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoApiRepositoryTest {

	@Autowired
	private TodoApiRepository todoApiRepository;

	@Test
	@DisplayName("Add Todo Items for user")
	@Order(1)
	@Rollback(value = false)
	public void testAddTodoItem() {
		Todo todo1 = Todo.builder().todoItem("Test-1").build();
		todoApiRepository.save(todo1);

		Todo todo2 = Todo.builder().todoItem("Test-2").isComplete(true).build();
		todoApiRepository.save(todo2);

		Todo todo3 = Todo.builder().todoItem("Test-3").build();
		todoApiRepository.save(todo3);

		Todo todo4 = Todo.builder().todoItem("Test-4").build();
		todoApiRepository.save(todo4);

		assertNotNull(todo1);
		assertFalse(todo1.isComplete());
		assertNotNull(todo1.getTodoDate());
		assertEquals("Test-1", todo1.getTodoItem());

		assertNotNull(todo2);
		assertTrue(todo2.isComplete());
		assertNotNull(todo2.getTodoDate());
		assertEquals("Test-2", todo2.getTodoItem());

		assertNotNull(todo3);
		assertFalse(todo3.isComplete());
		assertNotNull(todo3.getTodoDate());
		assertEquals("Test-3", todo3.getTodoItem());

		assertNotNull(todo4);
		assertFalse(todo4.isComplete());
		assertNotNull(todo4.getTodoDate());
		assertEquals("Test-4", todo4.getTodoItem());

	}

	@Test
	@DisplayName("Get the list of all todo items")
	@Order(2)
	public void testGetAllTodoItems() {
		List<Todo> todoList = (List<Todo>) todoApiRepository.findAll();

		assertNotNull(todoList);
		assertEquals(4, todoList.size());

	}

	@Test
	@DisplayName("Get the list of Todo Items marked as complete")
	@Order(3)
	public void testFindCompletedTodos() {
		List<Todo> todoList = (List<Todo>) todoApiRepository.findCompletedTodos();

		assertNotNull(todoList);
		assertEquals(1, todoList.size());

	}

	@Test
	@DisplayName("Remove Todo Item from the list")
	@Order(4)
	public void testRemoveTodoItem() {
		Todo todo3 = Todo.builder().todoItem("Test-3").id(Long.valueOf(3)).build();
		todoApiRepository.delete(todo3);

		List<Todo> todoList = (List<Todo>) todoApiRepository.findAll();

		assertNotNull(todoList);
		assertEquals(3, todoList.size());

	}

	@Test
	@DisplayName("Clear all Todo Item from the list")
	@Order(5)
	public void testClearAllTodoItem() {
		List<Todo> todoList = (List<Todo>) todoApiRepository.findAll();
		todoApiRepository.deleteAll(todoList);

		List<Todo> todoListAfterClear = (List<Todo>) todoApiRepository.findAll();
		assertEquals(Collections.EMPTY_LIST, todoListAfterClear);

	}

	@Test
	@DisplayName("Mark the Todo item as Complete")
	@Order(6)
	public void testUpdateTodoComplete() {
		Todo todo3 = Todo.builder().todoItem("Test-3").id(Long.valueOf(3)).isComplete(true).build();
		Todo updateTodo = todoApiRepository.save(todo3);

		assertNotNull(updateTodo);
		assertTrue(updateTodo.isComplete());

		List<Todo> todoList = (List<Todo>) todoApiRepository.findCompletedTodos();

		assertNotNull(todoList);
		assertEquals(2, todoList.size());

	}

}