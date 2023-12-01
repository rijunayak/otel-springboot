package com.example.order.inventoryservice.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    @GetMapping
    public ResponseEntity<Integer> getInventory() {
        return new ResponseEntity<>(Items.noOfItems, HttpStatus.OK);
    }

    @PutMapping
    public void consumeInventory(@RequestBody Integer noOfItemsBought) {
        if (noOfItemsBought <= 0) {
            log.error("Invalid items bought!");
            throw new IllegalArgumentException("Invalid items bought!");
        }

        Items.noOfItems -= noOfItemsBought;
    }
}
