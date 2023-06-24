package com.github.nut077.libraryeventsproducer.controller;

import com.github.nut077.libraryeventsproducer.domain.LibraryEvent;
import com.github.nut077.libraryeventsproducer.service.KafkaService;
import com.github.nut077.libraryeventsproducer.utility.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1")
public class LibraryEventsController {

  private final KafkaService kafkaService;

  @PostMapping("/library-event")
  public ResponseEntity<LibraryEvent> createLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {
    log.info("createLibraryEvent req: {}", ObjectMapperUtil.convertObjectToJsonString(libraryEvent));
    kafkaService.createLibraryEvent(libraryEvent);
    return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
  }

  @PutMapping("/library-event")
  public ResponseEntity<LibraryEvent> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {
    log.info("createLibraryEvent req: {}", ObjectMapperUtil.convertObjectToJsonString(libraryEvent));
    kafkaService.updateLibraryEvent(libraryEvent);
    return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
  }
}
