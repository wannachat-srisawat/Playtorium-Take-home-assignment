package com.example.discount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    private String category; // Coupon, On Top , Seasonal
    private Campaigns campaigns;
}