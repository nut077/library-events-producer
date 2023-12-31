package com.github.nut077.libraryeventsproducer.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.nut077.libraryeventsproducer.component.property.DateTimeFormatProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
//@Configuration
public class JacksonConfig {

  private final DateTimeFormatProperty props;

  @Bean
  public Jackson2ObjectMapperBuilder jacksonBuilder() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(props.getDateTime());
    JavaTimeModule timeModule = new JavaTimeModule();
    timeModule.addSerializer(
      OffsetDateTime.class,
      new JsonSerializer<>() {
        @Override
        public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
          jsonGenerator.writeString(dateTimeFormatter.format(offsetDateTime));
        }
      }
    );
    return Jackson2ObjectMapperBuilder.json()
      .modules(timeModule)
      .serializationInclusion(JsonInclude.Include.NON_EMPTY) // ไม่เอาค่าที่เป็น null หรือ empty ออกมา
      .featuresToEnable(SerializationFeature.INDENT_OUTPUT) // จัดรูปแบบ json
      .featuresToDisable(
        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
        SerializationFeature.FAIL_ON_EMPTY_BEANS,
        DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
        DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
      );
  }
}
