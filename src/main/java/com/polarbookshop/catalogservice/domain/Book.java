package com.polarbookshop.catalogservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Book(

    @Id
    Long id,

    @NotBlank(message = "The book ISBN must be defined.")
    @Pattern(
        regexp = "^([0-9]{10}|[0-9]{13})",
        message = "The ISBN format must be valid."
    )
    String isbn,

    @NotBlank(message = "The book title must be defined.")
    String title,

    @NotBlank(message = "The book author must be defined.")
    String author,

    @NotNull(message = "The book price must be defined.")
    @Positive(
        message = "The book price must be greater than zero."
    )
    Double price,

    @Version
    int version

) {
    public static Book of(
        String isbn, String title, String author, Double price
    ) {
        return new Book(
            null, isbn, title, author, price, 0
        );
    }
}
