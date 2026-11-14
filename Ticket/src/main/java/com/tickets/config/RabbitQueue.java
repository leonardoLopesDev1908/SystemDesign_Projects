package com.tickets.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;

@Configuration
public class RabbitQueue {

    public static final String QUEUE_NAME = "email-sender";

    @Bean
    public Queue getQueue(){
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
