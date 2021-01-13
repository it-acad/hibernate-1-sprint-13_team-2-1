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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTests {

    private static Validator validator;

    private static Role trainee;

    @BeforeAll
    static void init(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        trainee = new Role(1L, "Trainee", null);
    }

    @Test
    void createValidUser() {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setFirstName("Valid-Name");
        user.setLastName("Valid-Name");
        user.setPassword("V@LiDPa$$w0RD");
        user.setRole(trainee);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidId")
    void constraint_InvalidId(Long id) {
        User user = new User(
                id,
                "valid@email.com",
                "Valid-FirstName",
                "Valid-LastName",
                "V@LiDPa$$w0RD",
                trainee,
                null,
                null);
        user.setRole(trainee);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1L, violations.size());
    }
    private static Stream<Arguments> provideInvalidId(){
        return Stream.of(Arguments.of(0L), Arguments.of(-1L));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmailUser")
    void constraint_InvalidEmail(String input, int violationCount) {
        User user = new User();
        user.setEmail(input);
        user.setFirstName("Valid-Name");
        user.setLastName("Valid-Name");
        user.setPassword("V@LiDPa$$w0RD");
        user.setRole(trainee);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(violationCount, violations.size());
    }
    private static Stream<Arguments> provideInvalidEmailUser(){
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 1),
                Arguments.of("email@", 1),
                Arguments.of("invalid", 1),
                Arguments.of("invalidEmail", 1)
                );
    }


    @ParameterizedTest
    @MethodSource("provideInvalidFirstNameUser")
    void constraint_InvalidFirstName(String input, int violationCount) {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setFirstName(input);
        user.setLastName("Valid-LastName");
        user.setPassword("V@LiDPa$$w0RD");
        user.setRole(trainee);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(violationCount, violations.size());
    }
    private static Stream<Arguments> provideInvalidFirstNameUser(){
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 3),
                Arguments.of("invalid", 1),
                Arguments.of("Invalid-", 1),
                Arguments.of("Invalid-invalid", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLastNameUser")
    void constraint_InvalidLastName(String input, int violationCount) {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setFirstName("Valid-FirstName");
        user.setLastName(input);
        user.setPassword("V@LiDPa$$w0RD");
        user.setRole(trainee);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(violationCount, violations.size());
    }
    private static Stream<Arguments> provideInvalidLastNameUser(){
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 3),
                Arguments.of("invalid", 1),
                Arguments.of("Invalid-", 1),
                Arguments.of("Invalid-invalid", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPassword")
    void constraint_BadPassword(String input, int violationsCount) {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setFirstName("Valid-Name");
        user.setLastName("Valid-Name");
        user.setPassword(input);
        user.setRole(trainee);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(violationsCount, violations.size());
    }
    private static Stream<Arguments> provideInvalidPassword(){
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 1),
                Arguments.of("short", 1),
                Arguments.of("long".repeat(100), 1)
        );
    }

    @Test
    void testConstructor() {
        User user = new User(
                null,
                "",
                null,
                "bad_email",
                "short",
                null,
                null,
                null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(5, violations.size());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        String firstName = "John";
        Assertions.assertDoesNotThrow(() -> user.setFirstName(firstName));
        Assertions.assertEquals(firstName, user.getFirstName());

        String lastName = "Doe";
        Assertions.assertDoesNotThrow(() -> user.setLastName(lastName));
        Assertions.assertEquals(lastName, user.getLastName());

        String email = "valid@email.com";
        Assertions.assertDoesNotThrow(() -> user.setEmail(email));
        Assertions.assertEquals(email, user.getEmail());

        String password = "V@LiDPa$$w0RD";
        Assertions.assertDoesNotThrow(() -> user.setPassword(password));
        Assertions.assertEquals(password, user.getPassword());

        Assertions.assertDoesNotThrow(() -> user.setRole(trainee));
        Assertions.assertEquals(trainee, user.getRole());

        List<ToDo> todos = Collections.emptyList();
        Assertions.assertDoesNotThrow(() -> user.setTodos(todos));
        Assertions.assertEquals(todos, user.getTodos());

        List<ToDo> collaborations = Collections.emptyList();
        Assertions.assertDoesNotThrow(() -> user.setCollaborations(collaborations));
        Assertions.assertEquals(collaborations, user.getCollaborations());
    }

    @Test
    void testEqualsHashcodeToString() {
        User firstUser = new User();
        firstUser.setEmail("valid@email.com");
        firstUser.setFirstName("John");
        firstUser.setLastName("Doe");
        firstUser.setPassword("V@LiDPa$$w0RD");
        firstUser.setRole(trainee);

        User secondUser = new User();
        secondUser.setEmail("valid@email.com");
        secondUser.setFirstName("John");
        secondUser.setLastName("Doe");
        secondUser.setPassword("V@LiDPa$$w0RD");
        secondUser.setRole(trainee);

        User thirdUser = new User(
                1L,
                "John",
                "Doe",
                "valid@email.com",
                "V@LiDPa$$w0RD",
                trainee,
                null,
                null);

        Assertions.assertEquals(firstUser, secondUser);
        Assertions.assertEquals(firstUser.hashCode(), secondUser.hashCode());
        Assertions.assertEquals(firstUser.toString(), secondUser.toString());

        Assertions.assertNotEquals(firstUser, thirdUser);
        Assertions.assertNotEquals(firstUser, null);
        Assertions.assertNotEquals(firstUser, new Object());
        Assertions.assertEquals(firstUser.hashCode(), thirdUser.hashCode());
        Assertions.assertNotEquals(firstUser.toString(), thirdUser.toString());
    }



}
