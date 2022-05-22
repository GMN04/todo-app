package com.gmn.todo.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmn.todo.entity.Todo;
import com.gmn.todo.exception.TodoException;
import com.gmn.todo.service.TodoApiService;

@WebMvcTest(TodoApiController.class)
public class TodoApiControllerTest {
	@MockBean
	private TodoApiService todoApiService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Test
	@DisplayName("Happy Flow : Add new todo item api endpoint call.")
	void testAddTodoItem() throws Exception {
		Todo todo = Todo.builder().todoItem("Test-1").build();
		this.mockMvc.perform(post("/todo/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(todo))).andExpect(status().isCreated());

		Mockito.verify(todoApiService).addTodoItem(todo);
	}

	@Test
	@DisplayName("Endpoint not present.")
	void testAddTodoItem_NotFound() throws Exception {
		Todo todo = Todo.builder().todoItem("Test-1").build();
		this.mockMvc.perform(post("/todo/adding").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(todo))).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Todo passed as null - Add new todo item api endpoint call")
	void testAddTodoItem_Null() throws Exception {
		Todo todo = Todo.builder().todoItem(null).build();
		this.mockMvc
				.perform(post("/todo/add").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(todo)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.todoItem", is("todoItem cannot be null/blank")));

	}

	@Test
	@DisplayName("Remove todo item api endpoint call.")
	void testRemoveTodoItem() throws Exception {
		Long id = Long.valueOf(1);
		this.mockMvc.perform(delete("/todo/remove/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		Mockito.verify(todoApiService).removeTodoItem(1);
	}

	@Test
	@DisplayName("Mark todo item as complete api endpoint call.")
	void testMarkTodoComplete() throws Exception {
		Long id = Long.valueOf(1);
		this.mockMvc.perform(put("/todo/complete/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Mockito.verify(todoApiService).markTodoComplete(1);
	}

	@Test
	@DisplayName("Clear all available todo items api endpoint call.")
	@Order(4)
	void testClearTodoItems() throws Exception {
		this.mockMvc.perform(delete("/todo/clear").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		Mockito.verify(todoApiService).clearTodoItems();
	}

	@Test
	@DisplayName("Get all available todo items api endpoint call.")
	void testGetAllTodoItems() throws Exception {
		this.mockMvc.perform(get("/todo/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		Mockito.verify(todoApiService).getAllTodoItems(false);
	}

	@Test
	@DisplayName("Get all available todo items that are marked completed api endpoint call.")
	void testGetAllTodoItems_Complete() throws Exception {
		this.mockMvc.perform(get("/todo/?complete=true").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Mockito.verify(todoApiService).getAllTodoItems(true);
	}

	@Test
	@DisplayName("Runtime Exception on Get all available todo items api endpoint call.")
	void testGetAllTodoItems_RuntimeException() throws Exception {

		Mockito.when(todoApiService.getAllTodoItems(false)).thenThrow(RuntimeException.class);
		this.mockMvc.perform(get("/todo/").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.message", is("Oops ... We're sorry, our system ran in to some issues")));
		Mockito.verify(todoApiService).getAllTodoItems(false);
	}
}