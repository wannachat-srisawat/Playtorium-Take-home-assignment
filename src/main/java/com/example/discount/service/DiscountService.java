package com.example.discount.service;

import com.example.discount.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {
    public DiscountResult calculateFinalPrice(DiscountRequest request) {

        DiscountResult discountResult = new DiscountResult();

        boolean hasDuplicateCategory = request.getDiscounts().stream()
                .collect(Collectors.groupingBy(Discount::getCategory, Collectors.counting()))
                .values().stream()
                .anyMatch(count -> count > 1);

        if (hasDuplicateCategory) {
            discountResult.setMessage("Only one campaign is allowed per category.");
            return discountResult;
        }

        double finalPrice = 0.0;

        for (Item item : request.getItems()) {
            finalPrice += item.getPrice();
        }

        try {
            for (Discount discount : request.getDiscounts()) {
                switch (discount.getCategory()) {
                    case "Coupon" -> finalPrice -= getDiscountCoupon(discount.getCampaigns(), finalPrice);
                    case "On Top" -> finalPrice -= getDiscountOnTop(discount.getCampaigns(), request.getItems() ,finalPrice);
                    case "Seasonal" -> finalPrice -= getDiscountSeasonal(discount.getCampaigns(), finalPrice);
                    default -> {
                        discountResult.setMessage("Unsupported discount category : " + discount.getCategory());
                        return discountResult;
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            discountResult.setMessage(e.getMessage());
            return discountResult;
        }
        discountResult.setFinalPrice(finalPrice);
        discountResult.setMessage("Discount applied successfully");
        return discountResult;
    }

    private double getDiscountCoupon(Campaigns campaign, Double price) {
        String name = campaign.getName();

        switch (name) {
            case "Fixed Amount" -> {
                int amount = getIntParam(campaign, "Amount");
                return amount;
            }
            case "Percentage discount" -> {
                double percent = getDoubleParam(campaign, "Percentage");
                return price * (percent / 100.0);
            }
            default -> {
                throw new IllegalArgumentException("Sorry, we couldn't apply the selected coupon : " + name);
            }
        }
    }

    private double getDiscountOnTop(Campaigns campaign, List<Item> items ,Double price) {
        String name = campaign.getName();

        switch (name) {
            case "Percentage discount by item category" -> {
                String category = getStringParam(campaign, "Category");
                double percent = getDoubleParam(campaign, "Amount");

                double categoryTotal = items.stream()
                        .filter(item -> item.getCategory().equalsIgnoreCase(category))
                        .mapToDouble(Item::getPrice)
                        .sum();

                return categoryTotal * (percent / 100);
            }
            case "Discount by points" -> {
                double points = getDoubleParam(campaign, "CustomerPoints");
                double maxDiscount = price * 0.2;
                return Math.min(points, maxDiscount);
            }
            default -> {
                throw new IllegalArgumentException("Unsupported On Top campaign: " + name);
            }
        }
    }

    private double getDiscountSeasonal(Campaigns campaign, Double price) {
        int every = getIntParam(campaign, "Every");
        int discount = getIntParam(campaign, "Discount");
        if (price > every){
            int steps = (int) (price / every);
            return discount * steps;
        }else {
            throw new IllegalArgumentException("Cart total is less than minimum required for seasonal discount.");
        }
    }

    private double getDoubleParam(Campaigns campaign, String key) {
        Object value = campaign.getParameters().get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(value.toString());
    }

    private int getIntParam(Campaigns campaign, String key) {
        Object value = campaign.getParameters().get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    private String getStringParam(Campaigns campaign, String key) {
        Object value = campaign.getParameters().get(key);
        return value != null ? value.toString() : null;
    }

}