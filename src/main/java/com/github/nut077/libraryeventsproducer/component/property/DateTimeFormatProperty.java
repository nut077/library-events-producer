package com.github.nut077.libraryeventsproducer.component.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.formatdatetime")
public class DateTimeFormatProperty {

  private String date;
  private String dateTime;
}
