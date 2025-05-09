package com.example.discount.controller;

import com.example.discount.dto.DiscountRequest;
import com.example.discount.dto.DiscountResult;
import com.example.discount.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping("/calculate")
    public ResponseEntity<DiscountResult> calculate(@RequestBody DiscountRequest request) {
        return ResponseEntity.ok(discountService.calculateFinalPrice(request));
    }
}