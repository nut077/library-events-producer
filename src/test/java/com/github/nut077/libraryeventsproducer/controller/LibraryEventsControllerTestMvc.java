package com.github.nut077.libraryeventsproducer.controller;

import com.github.nut077.libraryeventsproducer.domain.Book;
import com.github.nut077.libraryeventsproducer.domain.LibraryEvent;
import com.github.nut077.libraryeventsproducer.domain.LibraryEventType;
import com.github.nut077.libraryeventsproducer.producer.LibraryEventsProducer;
import com.github.nut077.libraryeventsproducer.utility.ObjectMapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class)
class LibraryEventsControllerTestMvc {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LibraryEventsProducer libraryEventsProducer;

  @Test
  void should_success_createLibraryEvent() throws Exception {
    // given
    String json = ObjectMapperUtil.convertObjectToJsonString(getLibraryEvent());
    when(libraryEventsProducer.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class)))
      .thenReturn(null);

    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/v1/library-event")
      .content(json)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
  }

  @Test
  void should_fail_when_invalid_createLibraryEvent() throws Exception {
    // given
    String json = ObjectMapperUtil.convertObjectToJsonString(getLibraryEventInvalid());
    when(libraryEventsProducer.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class)))
      .thenReturn(null);

    String expectedMessageError = "book.bookAuthor - must not be blank, book.bookName - must not be blank";

    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/v1/library-event")
      .content(json)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is4xxClientError())
      .andExpect(content().string(expectedMessageError));
  }

  private LibraryEvent getLibraryEvent() {
    return new LibraryEvent(1, LibraryEventType.NEW, new Book(1, "world", "freedom"));
  }

  private LibraryEvent getLibraryEventInvalid() {
    return new LibraryEvent(1, LibraryEventType.NEW, new Book(1, "", ""));
  }
}