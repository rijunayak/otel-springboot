package com.example.order.orderservice.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    private static final String INVENTORY_URL = "http://inventory-service:8080/inventory";

    public OrderController(RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public void placeOrder(@RequestParam MultiValueMap<String, String> paramMap) {
        Integer noOfAvailableItems = restTemplate.getForObject(INVENTORY_URL, Integer.class);
        log.info("*********************** Number of available items: " + noOfAvailableItems + " *******************");

        Integer noOfItems = Integer.parseInt(paramMap.get("noOfItems").get(0));
        String gratitude = paramMap.get("gratitude").get(0);

        if (noOfItems <= noOfAvailableItems) {
            log.info("******************* Order placed for " + noOfItems + " ********************");
            restTemplate.put(INVENTORY_URL, noOfItems);
        } else {
            log.info("******************* " + noOfItems + " item(s) unavailable. Order not placed ********************");
        }

        Integer balance = restTemplate.getForObject(INVENTORY_URL, Integer.class);
        log.info("******************* Balance: " + balance + " ********************");

        log.info("***************** Sending gratitude ********************");
        rabbitTemplate.convertAndSend("", "q.gratitude", gratitude);
    }
}
