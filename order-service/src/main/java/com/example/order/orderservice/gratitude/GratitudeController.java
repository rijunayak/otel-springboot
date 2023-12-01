package com.example.order.orderservice.gratitude;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gratitude-ack")
@Slf4j
public class GratitudeController {

    @GetMapping
    public ResponseEntity<String> ackGratitude() {
        log.info("**************** Acknowledge Gratitude *********************");
        return new ResponseEntity<>("Gratitude Acknowledged!", HttpStatus.OK);
    }
}
