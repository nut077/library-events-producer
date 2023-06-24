package com.github.nut077.libraryeventsproducer.service;

import com.github.nut077.libraryeventsproducer.domain.LibraryEvent;
import com.github.nut077.libraryeventsproducer.domain.LibraryEventType;
import com.github.nut077.libraryeventsproducer.exception.BadRequestException;
import com.github.nut077.libraryeventsproducer.producer.LibraryEventsProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaService {

  private final LibraryEventsProducer libraryEventsProducer;

  public CompletableFuture<SendResult<String, String>> createLibraryEvent(LibraryEvent libraryEvent) {
    return libraryEventsProducer.sendLibraryEventWithProducerRecord(libraryEvent);
  }

  public CompletableFuture<SendResult<String, String>> updateLibraryEvent(LibraryEvent libraryEvent) {
    if (libraryEvent.id() == null) {
      throw new BadRequestException("Please pass the id");
    }
    if (!libraryEvent.libraryEventType().equals(LibraryEventType.UPDATE)) {
      throw new BadRequestException("Only UPDATE event type is supported");
    }
    return libraryEventsProducer.sendLibraryEventWithProducerRecord(libraryEvent);
  }
}
