package com.softserve.itacademy.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ToDoTests {
	private static Validator validator;
	private static User validUserWithRole;

	@BeforeAll
	static void init(){
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		validUserWithRole = new User(
				1L,
				"valid@email.com",
				"First-Name",
				"Last-Name",
				"password",
				new Role(1L, "DEVELOPER", null),
				new ArrayList<>(),
				Collections.emptyList());

	}

	@Test
	void createValidToDo() {
		ToDo validToDo = new ToDo();
		validToDo.setTitle("build project");
		validToDo.setOwner(null);
		validToDo.setCreatedAt(LocalDateTime.now());
		validToDo.setTasks(Collections.emptyList());
		validToDo.setCollaborators(Collections.emptyList());

		Set<ConstraintViolation<ToDo>> violations = validator.validate(validToDo);
		Assertions.assertEquals(0, violations.size());
	}

	@ParameterizedTest
	@MethodSource("provideInvalidId")
	void constraint_InvalidId(Long invalidId) {
		ToDo invalidIdToDo = new ToDo(
				invalidId,
				"build project",
				LocalDateTime.now(),
				validUserWithRole,
				Collections.emptyList(),
				Collections.emptyList());

		Set<ConstraintViolation<ToDo>> violations = validator.validate(invalidIdToDo);
		assertEquals(1L, violations.size());
	}
	private static Stream<Arguments> provideInvalidId(){
		return Stream.of(Arguments.of(0L), Arguments.of(-1L));
	}

	@ParameterizedTest
	@MethodSource("provideInvalidTitle")
	void constraint_TitleNotBlank(String title, int violationsCound) {
		ToDo invalidIdToDo = new ToDo();
		invalidIdToDo.setTitle(title);
		invalidIdToDo.setCreatedAt(LocalDateTime.now());
		invalidIdToDo.setOwner(validUserWithRole);
		invalidIdToDo.setTasks(Collections.emptyList());
		invalidIdToDo.setCollaborators(Collections.emptyList());

		Set<ConstraintViolation<ToDo>> violations = validator.validate(invalidIdToDo);
		assertEquals(violationsCound, violations.size());
	}
	private static Stream<Arguments> provideInvalidTitle(){
		return Stream.of(
				Arguments.of(null, 1),
				Arguments.of("", 1),
				Arguments.of(" ", 1),
				Arguments.of("\t", 1)
		);
	}

	@Test
	void testConstructor() {
		ToDo invalidIdToDo = new ToDo(
				0L,
				"",
				null,
				null,
				null,
				null);
		Set<ConstraintViolation<ToDo>> violations = validator.validate(invalidIdToDo);
		assertEquals(2, violations.size());
	}

	@Test
	void testSettersAndGetters() {
		ToDo todo = new ToDo();

		String title = "build project";
		Assertions.assertDoesNotThrow(() -> todo.setTitle(title));
		Assertions.assertEquals(title, todo.getTitle());

		Assertions.assertDoesNotThrow(() -> todo.setOwner(validUserWithRole));
		Assertions.assertEquals(validUserWithRole, todo.getOwner());

		LocalDateTime createdAt = LocalDateTime.now();
		Assertions.assertDoesNotThrow(() -> todo.setCreatedAt(createdAt));
		Assertions.assertEquals(createdAt, todo.getCreatedAt());

		List<Task> tasks = new ArrayList<>();
		Assertions.assertDoesNotThrow(() -> todo.setTasks(tasks));
		Assertions.assertEquals(tasks, todo.getTasks());

		List<User> collaborators = new ArrayList<>();
		Assertions.assertDoesNotThrow(() -> todo.setCollaborators(collaborators));
		Assertions.assertEquals(collaborators, todo.getCollaborators());
	}

	@Test
	void testEqualsHashcodeToString() {
		LocalDateTime now = LocalDateTime.now();

		ToDo todo1 = new ToDo();
		todo1.setTitle("build project");
		todo1.setCreatedAt(now);
		todo1.setOwner(validUserWithRole);
		todo1.setTasks(Collections.emptyList());
		todo1.setCollaborators(Collections.emptyList());

		ToDo todo2 = new ToDo();
		todo2.setTitle("build project");
		todo2.setCreatedAt(now);
		todo2.setOwner(validUserWithRole);
		todo2.setTasks(Collections.emptyList());
		todo2.setCollaborators(Collections.emptyList());

		ToDo todo3 = new ToDo(
				1L,
				"build project",
				now,
				validUserWithRole,
				Collections.emptyList(),
				Collections.emptyList());

		Assertions.assertEquals(todo1, todo1);
		Assertions.assertEquals(todo1, todo2);
		Assertions.assertEquals(todo1.hashCode(), todo2.hashCode());
		Assertions.assertEquals(todo1.toString(), todo2.toString());

		Assertions.assertNotEquals(todo1, todo3);
		Assertions.assertNotEquals(todo1, null);
		Assertions.assertNotEquals(todo1, new Object());
		Assertions.assertEquals(todo1.hashCode(), todo3.hashCode());
		Assertions.assertNotEquals(todo1.toString(), todo3.toString());

	}
}
