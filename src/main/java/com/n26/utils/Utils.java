package com.n26.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class Utils {

    @Bean
    public Clock clock () {
        return Clock.systemUTC();
    }
}
