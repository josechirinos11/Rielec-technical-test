package com.rielec.machine.test.backend.application.beans;

import com.rielec.machine.test.backend.application.utils.DateToTimestampConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class DateBeans {
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(new DateToTimestampConverter()));
    }
}
