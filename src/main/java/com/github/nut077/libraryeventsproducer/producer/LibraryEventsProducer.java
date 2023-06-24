package com.github.nut077.libraryeventsproducer.producer;

import com.github.nut077.libraryeventsproducer.component.property.KafkaProperty;
import com.github.nut077.libraryeventsproducer.domain.LibraryEvent;
import com.github.nut077.libraryeventsproducer.utility.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryEventsProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaProperty kafkaProperty;

  public CompletableFuture<SendResult<String, String>> sendLibraryEvent(LibraryEvent libraryEvent) {
    String key = getKey(libraryEvent.id());
    String value = ObjectMapperUtil.convertObjectToJsonString(libraryEvent);
    var resultCompletableFuture = kafkaTemplate.send(kafkaProperty.getTopic(), key, value);
    return resultCompletableFuture.whenComplete((sendResult, throwable) -> {
      if (throwable != null) {
        handleFailure(key, value, throwable);
      } else {
        handleSuccess(key, value, sendResult);
      }
    });
  }

  public SendResult<String, String> sendLibraryEventSync(LibraryEvent libraryEvent) throws ExecutionException, InterruptedException, TimeoutException {
    String key = getKey(libraryEvent.id());
    String value = ObjectMapperUtil.convertObjectToJsonString(libraryEvent);
    SendResult<String, String> sendResult = kafkaTemplate.send(kafkaProperty.getTopic(), key, value)
      .get(3, TimeUnit.SECONDS);
    handleSuccess(key, value, sendResult);
    return sendResult;
  }

  public CompletableFuture<SendResult<String, String>> sendLibraryEventWithProducerRecord(LibraryEvent libraryEvent) {
    String key = getKey(libraryEvent.id());
    String value = ObjectMapperUtil.convertObjectToJsonString(libraryEvent);
    var producerRecord = buildProducerRecord(key, value);
    var resultCompletableFuture = kafkaTemplate.send(producerRecord);
    return resultCompletableFuture.whenComplete((sendResult, throwable) -> {
      if (throwable != null) {
        handleFailure(key, value, throwable);
      } else {
        handleSuccess(key, value, sendResult);
      }
    });
  }

  private String getKey(Long id) {
    String key = null;
    if (id != null) {
      key = id.toString();
    }
    return key;
  }

  private ProducerRecord<String, String> buildProducerRecord(String key, String value) {
    List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
    return new ProducerRecord<>(kafkaProperty.getTopic(), null, key, value, recordHeaders);
  }

  private void handleSuccess(String key, String value, SendResult<String, String> sendResult) {
    log.info("Message sent successfully for the key: {} and the value: {} , partition is {}", key, value, sendResult.getRecordMetadata().partition());
  }

  private void handleFailure(String key, String value, Throwable throwable) {
    log.error("Error sending the message and the exception is {}", throwable.getMessage(), throwable);
  }
}
