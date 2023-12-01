package com.example.order.orderservice.order.dto;

public class OrderRequestDto {
    private final Integer noOfItems;

    public OrderRequestDto(Integer noOfItems) {
        this.noOfItems = noOfItems;
    }

    public Integer getNoOfItems() {
        return noOfItems;
    }
}
