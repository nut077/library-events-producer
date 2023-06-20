package com.github.nut077.libraryeventsproducer.domain;

import jakarta.validation.constraints.NotBlank;

public record Book(Integer bookId, @NotBlank String bookName, @NotBlank String bookAuthor) {
}
