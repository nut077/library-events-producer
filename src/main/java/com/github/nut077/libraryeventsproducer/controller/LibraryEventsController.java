package com.github.nut077.libraryeventsproducer.controller;

import com.github.nut077.libraryeventsproducer.domain.LibraryEvent;
import com.github.nut077.libraryeventsproducer.producer.LibraryEventsProducer;
import com.github.nut077.libraryeventsproducer.utility.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
public class LibraryEventsController {

  private final LibraryEventsProducer libraryEventsProducer;

  @PostMapping("/library-event")
  public ResponseEntity<LibraryEvent> createLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws ExecutionException, InterruptedException, TimeoutException {
    log.info("createLibraryEvent req: {}", ObjectMapperUtil.convertObjectToJsonString(libraryEvent));
    //libraryEventsProducer.sendLibraryEvent(libraryEvent);
    //libraryEventsProducer.sendLibraryEventSync(libraryEvent);
    libraryEventsProducer.sendLibraryEventWithProducerRecord(libraryEvent);
    return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
  }
}
