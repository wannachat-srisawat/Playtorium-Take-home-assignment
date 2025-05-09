package com.example.discount.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiscountRequest {
    private List<Item> items;
    private List<Discount> discounts;
}