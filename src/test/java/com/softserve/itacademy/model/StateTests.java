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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StateTests {

    private static Validator validator;


    @BeforeAll
    static void init(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createValidState() {
        State state = new State();
        state.setName("ACTIVE");
        state.setTasks(Collections.emptyList());

        Set<ConstraintViolation<State>> violations = validator.validate(state);
        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidId")
    void constraint_InvalidId(Long id) {
        State state = new State(id, "ACTIVE", Collections.emptyList());

        Set<ConstraintViolation<State>> violations = validator.validate(state);
        assertEquals(1L, violations.size());
    }
    private static Stream<Arguments> provideInvalidId(){
        return Stream.of(Arguments.of(0L), Arguments.of(-1L));
    }


    @ParameterizedTest
    @MethodSource("provideInvalidTitle")
    void constraint_InvalidName(String input, int violationCount) {
        State state = new State(1L, input, Collections.emptyList());

        Set<ConstraintViolation<State>> violations = validator.validate(state);
        assertEquals(violationCount, violations.size());
    }
    private static Stream<Arguments> provideInvalidTitle(){
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 1),
                Arguments.of("\t", 1),
                Arguments.of("bad@state%name", 1),
                Arguments.of("плохое_имя_состояния", 1)
        );
    }


    @Test
    void testConstructor() {
        State state = new State(0L, null, Collections.emptyList());

        Set<ConstraintViolation<State>> violations = validator.validate(state);
        assertEquals(2, violations.size());
    }


    @Test
    void testSettersAndGetters() {
        State state = new State();
        String name = "ACTIVE";
        assertDoesNotThrow(() -> state.setName(name));
        assertEquals(name, state.getName());

        List<Task> tasks = Collections.emptyList();
        assertDoesNotThrow(() -> state.setTasks(tasks));
        assertEquals(tasks, state.getTasks());
    }

    @Test
    void testEqualsHashcodeToString() {
        State state1 = new State(1L, "ACTIVE", Collections.emptyList());
        State state2 = new State(1L, "ACTIVE", Collections.emptyList());

        State state3 = new State(2L, "INACTIVE", Collections.emptyList());
        State state4 = new State(3L, null, Collections.emptyList());

        Assertions.assertEquals(state1, state1);
        Assertions.assertEquals(state1, state2);
        Assertions.assertEquals(state1.hashCode(), state2.hashCode());
        Assertions.assertEquals(state1.toString(), state2.toString());

        Assertions.assertNotEquals(state1, state3);
        Assertions.assertNotEquals(state4, state1);
        Assertions.assertNotEquals(state1, null);
        Assertions.assertNotEquals(state1, new Object());
        Assertions.assertNotEquals(state1.hashCode(), state3.hashCode());
        Assertions.assertNotEquals(state1.hashCode(), state4.hashCode());

        Assertions.assertNotEquals(state1.toString(), state3.toString());
    }


}
