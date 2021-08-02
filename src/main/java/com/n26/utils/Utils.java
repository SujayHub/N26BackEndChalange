package com.n26.utils;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Utils {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
