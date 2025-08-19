package com.sankalp.projects.airBnbApp.strategy;

import com.sankalp.projects.airBnbApp.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
