package com.github.nut077.libraryeventsproducer.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class ObjectToJsonStringUtil {

  @SneakyThrows
  public static String convert(Object obj) {
    return new ObjectMapper().writeValueAsString(obj);
  }
}
