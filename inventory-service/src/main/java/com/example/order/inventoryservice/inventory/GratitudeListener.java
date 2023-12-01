package com.example.order.inventoryservice.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GratitudeListener {

    private final RestTemplate restTemplate;

    private static final String ORDER_GRATITUDE_URL = "http://order-service:8080/gratitude-ack";

    public GratitudeListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RabbitListener(queues = {"q.gratitude"})
    public void acknowledgeGratitude(String gratitude) {
        log.info("****************** Received gratitude: \"" + gratitude + "\" ***********************");
        log.info("****************** Getting Order ***********************");
        String response = restTemplate.getForObject(ORDER_GRATITUDE_URL, String.class);
        log.info("****************** Gratitude Acknowledgement Response: " + response + " ***********************");
    }
}
