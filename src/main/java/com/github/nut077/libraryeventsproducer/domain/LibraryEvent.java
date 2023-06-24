package com.github.nut077.libraryeventsproducer.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent(Long id, LibraryEventType libraryEventType, @NotNull @Valid Book book) {
}
