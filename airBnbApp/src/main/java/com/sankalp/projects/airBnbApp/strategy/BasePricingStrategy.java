package com.sankalp.projects.airBnbApp.strategy;

import com.sankalp.projects.airBnbApp.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor

public class BasePricingStrategy implements  PricingStrategy{


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
       return inventory.getRoom().getBasePrice() ;
    }
}
