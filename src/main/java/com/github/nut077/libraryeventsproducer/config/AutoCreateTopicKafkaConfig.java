package com.github.nut077.libraryeventsproducer.config;

import com.github.nut077.libraryeventsproducer.component.property.KafkaProperty;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class AutoCreateTopicKafkaConfig {

  private final KafkaProperty kafkaProperty;

  @Bean
  public NewTopic libraryEvents() {
    return TopicBuilder
      .name(kafkaProperty.getTopic())
      .partitions(3)
      .replicas(3)
      .build();
  }
}
