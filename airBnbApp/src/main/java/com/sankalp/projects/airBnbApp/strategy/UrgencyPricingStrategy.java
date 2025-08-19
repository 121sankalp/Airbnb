package com.sankalp.projects.airBnbApp.strategy;

import com.sankalp.projects.airBnbApp.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;



@RequiredArgsConstructor

public class UrgencyPricingStrategy implements  PricingStrategy{
    private  final PricingStrategy wrappedStartegy ;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
     BigDecimal price =    wrappedStartegy.calculatePrice(inventory) ;
     LocalDate today = LocalDate.now() ;
     if(!inventory.getDate().isBefore(today)&&inventory.getDate().isBefore(today.plusDays(7)))
     {
         price = price.multiply(BigDecimal.valueOf(1.2)) ;
     }

     return  price ;
    }
}
