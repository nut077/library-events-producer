package com.github.nut077.libraryeventsproducer.domain;

import jakarta.validation.constraints.NotBlank;

public record Book(Long id, @NotBlank String bookName, @NotBlank String bookAuthor) {
}
