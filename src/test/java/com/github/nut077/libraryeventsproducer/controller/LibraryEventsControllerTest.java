package com.github.nut077.libraryeventsproducer.controller;

import com.github.nut077.libraryeventsproducer.domain.Book;
import com.github.nut077.libraryeventsproducer.domain.LibraryEvent;
import com.github.nut077.libraryeventsproducer.domain.LibraryEventType;
import com.github.nut077.libraryeventsproducer.utility.ObjectMapperUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = "library-events")
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
  "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
class LibraryEventsControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  private Consumer<String, String> consumer;

  @BeforeEach
  void setUp() {
    var configs = KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer())
      .createConsumer();
    embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
  }

  @AfterEach
  void tearDown() {
    consumer.close();
  }

  @Test
  void should_success_createLibraryEvent() {
    // given
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
    var httpEntity = new HttpEntity<>(getLibraryEvent(), httpHeaders);

    // when
    var actual = restTemplate.exchange("/v1/library-event", HttpMethod.POST, httpEntity, LibraryEvent.class);

    // then
    assertThat(actual.getStatusCode(), is(HttpStatus.CREATED));

    var consumerRecords = KafkaTestUtils.getRecords(consumer);
    assertEquals(1, consumerRecords.count());

    consumerRecords.forEach(record -> {
      LibraryEvent libraryEvent = ObjectMapperUtil.convertJsonStringToObject(record.value(), LibraryEvent.class);
      assertEquals(libraryEvent, getLibraryEvent());
    });
  }

  private LibraryEvent getLibraryEvent() {
    return new LibraryEvent(1L, LibraryEventType.NEW, new Book(1L, "world", "freedom"));
  }

}