package com.polarbookshop.catalogservice.domain;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BookValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var book =
            Book.of("1234567890", "Title", "Author",  9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnIsBlankThenTwoViolationsReturned() {
        var book =
            Book.of("", "Title", "Author", 9.90);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        assertTrue(
            violations.stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("The book ISBN must be defined.")));

        assertTrue(
            violations.stream()
                .map(ConstraintViolation::getMessage)
                .anyMatch(message -> message.equals("The ISBN format must be valid.")));
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        var book =
            Book.of("a223456789", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    void whenTitleIsBlankThenValidationFails() {
        var book =
            Book.of("1234567890", "", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The book title must be defined.");
    }

    @Test
    void whenTitleAndAuthorAreBlankThenTwoViolationsReturned() {
        var book =
            Book.of("1234567890", "", "", 9.90);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);

        assertTrue(
                violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .anyMatch(message -> message.equals("The book title must be defined.")));

        assertTrue(
                violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .anyMatch(message -> message.equals("The book author must be defined.")));
    }

    @Test
    void whenPriceIsBlankThenValidationFails() {
        var book =
                Book.of("1234567890", "Title", "Author", null);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be defined.");
    }

    @Test
    void whenPriceIsNegativeThenValidationFails() {
        var book =
                Book.of("1234567890", "Title", "Author", -90.0);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }
}
