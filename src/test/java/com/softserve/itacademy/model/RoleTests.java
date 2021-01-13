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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RoleTests {
    private static Validator validator;

    @BeforeAll
    static void init(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createValidRole() {
        Role role = new Role();
        role.setName("DEVELOPER");
        role.setUsers(Collections.emptyList());

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidId")
    void constraint_InvalidId(Long id) {
        Role role = new Role(id, "DEVELOPER", Collections.emptyList());

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertEquals(1L, violations.size());
    }
    private static Stream<Arguments> provideInvalidId(){
        return Stream.of(Arguments.of(0L), Arguments.of(-1L));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRoleName")
    void constraintViolationOnEmptyRoleName(String data, int violationsExpected) {
        Role emptyTitleRole = new Role();
        emptyTitleRole.setName(data);
        emptyTitleRole.setUsers(Collections.emptyList());

        Set<ConstraintViolation<Role>> violations = validator.validate(emptyTitleRole);
        assertEquals(violationsExpected, violations.size());
    }
    private static Stream<Arguments> provideInvalidRoleName(){
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 1),
                Arguments.of(" ", 1),
                Arguments.of("\t", 1)
        );
    }

    @Test
    void testConstructor() {
        Role role = new Role(-1L, "\t", Collections.emptyList());
        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertEquals(2, violations.size());
    }

    @Test
    void testSettersAndGetters() {
        Role role = new Role();
        Assertions.assertNull(role.getId());

        String name = "DEVELOPER";
        Assertions.assertDoesNotThrow(() -> role.setName(name));
        Assertions.assertEquals(name, role.getName());

        List<User> users = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> role.setUsers(users));
        Assertions.assertEquals(users, role.getUsers());
    }

    @Test
    void testEqualsHashcodeToString() {
        Role role1 = new Role(1L, "DEVELOPER", Collections.emptyList());
        Role role2 = new Role(1L, "DEVELOPER", Collections.emptyList());

        Role role3 = new Role(2L, "TESTER", Collections.emptyList());
        Role role4 = new Role(3L, null, Collections.emptyList());

        Assertions.assertEquals(role1, role1);
        Assertions.assertEquals(role1, role2);
        Assertions.assertEquals(role1.hashCode(), role2.hashCode());
        Assertions.assertEquals(role1.toString(), role2.toString());

        Assertions.assertNotEquals(role1, role3);
        Assertions.assertNotEquals(role4, role1);
        Assertions.assertNotEquals(role1, null);
        Assertions.assertNotEquals(role1, new Object());
        Assertions.assertNotEquals(role1.hashCode(), role3.hashCode());
        Assertions.assertNotEquals(role1.hashCode(), role4.hashCode());

        Assertions.assertNotEquals(role1.toString(), role3.toString());
    }

}
