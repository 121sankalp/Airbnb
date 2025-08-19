package com.sankalp.projects.airBnbApp.strategy;

import com.sankalp.projects.airBnbApp.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@RequiredArgsConstructor

public class OccupancyPricingStrategy implements  PricingStrategy{
    private final PricingStrategy wrapStrategy ;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {

      BigDecimal price = wrapStrategy.calculatePrice(inventory) ;
      double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount() ;
      if(occupancyRate>0.8)
      {
          price = price.multiply(BigDecimal.valueOf(1.5)) ;
      }
      return  price ;
    }
}
