package com.gmn.todo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.gmn.todo.entity.Todo;
import com.gmn.todo.exception.TodoException;
import com.gmn.todo.repository.TodoApiRepository;

@SpringBootTest
public class TodoApiServiceImplTest {

	@MockBean
	private TodoApiRepository todoApiRepository;

	@Autowired
	private TodoApiService todoApiService;

	@Test
	@DisplayName("Happy Flow - Get all todo items available")
	void testGetAllTodoItems() throws Exception {
		Todo todoMockResponse1 = Todo.builder().id(Long.valueOf(1)).isComplete(false).todoItem("Test-1").build();
		Todo todoMockResponse2 = Todo.builder().id(Long.valueOf(2)).isComplete(true).todoItem("Test-2").build();
		List<Todo> todoList= new ArrayList<>();
		todoList.add(todoMockResponse1);
		todoList.add(todoMockResponse2);
		when(todoApiRepository.findAll()).thenReturn(todoList);
		List<Todo> todoResp = todoApiService.getAllTodoItems(false);
		assertNotNull(todoResp);
		assertEquals(2,todoList.size());
	}
	
	@Test
	@DisplayName("Happy Flow - Get todo items marked as completed")
	void testGetAllTodoItems_Completed() throws Exception {
		Todo todoMockResponse2 = Todo.builder().id(Long.valueOf(2)).isComplete(true).todoItem("Test-2").build();
		List<Todo> todoList= new ArrayList<>();
		todoList.add(todoMockResponse2);
		when(todoApiRepository.findCompletedTodos()).thenReturn(todoList);
		List<Todo> todoResp = todoApiService.getAllTodoItems(true);
		assertNotNull(todoResp);
		assertEquals(1,todoList.size());
	}
	
	@Test
	@DisplayName("Happy Flow - Add new todo item")
	void testAddTodoItem() throws Exception {
		Todo todo = Todo.builder().todoItem("Test-1").build();
		Todo todoMockResponse = Todo.builder().id(Long.valueOf(1)).isComplete(false).todoItem("Test-1").build();
		when(todoApiRepository.save(todo)).thenReturn(todoMockResponse);
		Todo todoResp = todoApiService.addTodoItem(todo);
		assertNotNull(todoResp);
	}

	@Test
	@DisplayName("Negative Flow - Add duplicate todo item")
	void testAddTodoItem_DuplicateItem() throws Exception {
		Todo todo = Todo.builder().todoItem("Test-1").build();
		Todo todoMockResponse = Todo.builder().id(Long.valueOf(1)).isComplete(false).todoItem("Test-1").build();
		when(todoApiRepository.findTodoByName(todo.getTodoItem())).thenReturn(todoMockResponse);
		TodoException exception = assertThrows(TodoException.class, () -> {
			todoApiService.addTodoItem(todo);
		});
		assertEquals("TODO item already Exist", exception.getMessage());
	}

	@Test
	@DisplayName("Happy Flow - Remove todo item")
	void testRemoveTodoItem() throws Exception {
		Todo todoMockResponse = Todo.builder().id(Long.valueOf(1)).isComplete(false).todoItem("Test-1").build();
		when(todoApiRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(todoMockResponse));
		todoApiService.removeTodoItem(Long.valueOf(1));
		Mockito.verify(todoApiRepository).delete(todoMockResponse);

	}
	
	@Test
	@DisplayName("Happy Flow - Clear all todo item")
	void testClearTodoItem() throws Exception {
		todoApiService.clearTodoItems();
		Mockito.verify(todoApiRepository).deleteAll();

	}

	@Test
	@DisplayName("Negative Flow - Remove todo item not found")
	void testRemoveTodoItem_IdNotFound() throws Exception {
		when(todoApiRepository.findById(Long.valueOf(1))).thenReturn(Optional.empty());

		TodoException exception = assertThrows(TodoException.class, () -> {
			todoApiService.removeTodoItem(Long.valueOf(1));
		});
		assertEquals("TODO item doesn't exist", exception.getMessage());

	}
	
	@Test
	@DisplayName("Happy Flow - Mark todo item as complete")
	void testMarkTodoComplete() throws Exception {
		Todo todoMockResponse = Todo.builder().id(Long.valueOf(1)).isComplete(false).todoItem("Test-1").build();
		when(todoApiRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(todoMockResponse));
		todoApiService.markTodoComplete(Long.valueOf(1));
		Mockito.verify(todoApiRepository).save(todoMockResponse);

	}

	@Test
	@DisplayName("Negative Flow - Mark todo item as complete, item not found")
	void testMarkTodoComplete_IdNotFound() throws Exception {
		when(todoApiRepository.findById(Long.valueOf(1))).thenReturn(Optional.empty());

		TodoException exception = assertThrows(TodoException.class, () -> {
			todoApiService.markTodoComplete(Long.valueOf(1));
		});
		assertEquals("TODO item doesn't exist", exception.getMessage());

	}

}
