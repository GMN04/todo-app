package com.gmn.todo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * The Entity class for managing Todo data
 * 
 * @author GMN
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Valid
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "todoItem cannot be null/blank")
	private String todoItem;

//	@Column(nullable = false, updatable = false)
//	@CreationTimestamp
//	private LocalDateTime todoDate;

	@Column(name = "todoDate", updatable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private final Date todoDate = new Date();

	private boolean isComplete;

}
