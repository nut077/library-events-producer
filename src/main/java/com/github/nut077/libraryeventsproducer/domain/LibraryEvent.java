package com.github.nut077.libraryeventsproducer.domain;

public record LibraryEvent(Integer libraryEventId, LibraryEventType libraryEventType, Book book) {
}
