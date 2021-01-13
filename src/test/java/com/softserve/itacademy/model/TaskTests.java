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
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskTests {
    private static Validator validator;
    private static ToDo validToDoForTask;
    private static State validStateForTask;

    @BeforeAll
    static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        validToDoForTask = new ToDo(
                101l,
                "todo#1",
                LocalDateTime.now(),
                new User(),
                new ArrayList<Task>(),
                new ArrayList<User>());
        validStateForTask = new State(111l, "state", new ArrayList<Task>());
    }

    @Test
    void createValidTask() {
        Task validTask = new Task();
        validTask.setName("task#1");
        validTask.setPriority(Priority.HIGH);
        validTask.setTodo(validToDoForTask);
        validTask.setState(new State());

        Set<ConstraintViolation<Task>> violations = validator.validate(validTask);
        Assertions.assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidId")
    void constraint_InvalidId(Long invalidId) {
        Task invalidIdTask = new Task(
                invalidId,
                "task#1",
                Priority.HIGH,
                validToDoForTask,
                validStateForTask);

        Set<ConstraintViolation<Task>> violations = validator.validate(invalidIdTask);
        assertEquals(1L, violations.size());
    }

    private static Stream<Arguments> provideInvalidId() {
        return Stream.of(Arguments.of(0L), Arguments.of(-1L));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidName")
    void constraint_TitleNotBlank(String title, int violationsCount) {
        Task invalidNameTask = new Task();
        invalidNameTask.setName(title);
        invalidNameTask.setTodo(validToDoForTask);
        invalidNameTask.setState(validStateForTask);
        invalidNameTask.setPriority(Priority.MEDIUM);

        Set<ConstraintViolation<Task>> violations = validator.validate(invalidNameTask);
        assertEquals(violationsCount, violations.size());
    }
    private static Stream<Arguments> provideInvalidName() {
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 1),
                Arguments.of(" ", 1),
                Arguments.of("\t", 1)
        );
    }

    @Test
    void testConstructor() {
        Task invalidTask = new Task(
                0L,
                "",
                Priority.HIGH,
                validToDoForTask,
                validStateForTask);
        Set<ConstraintViolation<Task>> violations = validator.validate(invalidTask);
        assertEquals(2, violations.size());
    }

    @Test
    void testSettersAndGetters() {
        Task task = new Task();

        String name = "task#2";
        Assertions.assertDoesNotThrow(() -> task.setName(name));
        Assertions.assertEquals(name, task.getName());

        Assertions.assertDoesNotThrow(() -> task.setPriority(Priority.MEDIUM));
        Assertions.assertEquals(Priority.MEDIUM, task.getPriority());

        Assertions.assertDoesNotThrow(() -> task.setTodo(validToDoForTask));
        Assertions.assertEquals(validToDoForTask, task.getTodo());

        Assertions.assertDoesNotThrow(() -> task.setState(validStateForTask));
        Assertions.assertEquals(validStateForTask, task.getState());
    }

}
