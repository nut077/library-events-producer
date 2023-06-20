package com.github.nut077.libraryeventsproducer.controller;

import com.github.nut077.libraryeventsproducer.domain.LibraryEvent;
import com.github.nut077.libraryeventsproducer.producer.LibraryEventsProducer;
import com.github.nut077.libraryeventsproducer.utility.ObjectToJsonStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
public class LibraryEventsController {

  private final LibraryEventsProducer libraryEventsProducer;

  @PostMapping("/library-event")
  public ResponseEntity<LibraryEvent> createLibraryEvent(@RequestBody LibraryEvent libraryEvent) {
    log.info("createLibraryEvent req: {}", ObjectToJsonStringUtil.convert(libraryEvent));
    libraryEventsProducer.sendLibraryEvent(libraryEvent);
    return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
  }
}
