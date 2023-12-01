package com.example.order.orderservice.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RmqConfig {

    @Bean
    public Queue createGratitudeQueue() {
        return new Queue("q.gratitude");
    }
}
