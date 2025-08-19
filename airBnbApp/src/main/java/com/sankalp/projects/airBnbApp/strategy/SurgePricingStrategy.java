package com.sankalp.projects.airBnbApp.strategy;

import com.sankalp.projects.airBnbApp.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@RequiredArgsConstructor

public class SurgePricingStrategy implements  PricingStrategy {

private  final PricingStrategy wrapped ;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        return price.multiply(inventory.getSurgeFactor());
    }
}
