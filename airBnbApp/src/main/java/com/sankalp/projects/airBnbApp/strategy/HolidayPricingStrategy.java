package com.sankalp.projects.airBnbApp.strategy;

import com.sankalp.projects.airBnbApp.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@RequiredArgsConstructor

public class HolidayPricingStrategy implements  PricingStrategy{

    private final PricingStrategy wrappedStrategy ;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
      BigDecimal price = wrappedStrategy.calculatePrice(inventory) ;

      boolean isTodayHoliday = true ;

      if(isTodayHoliday)
      {
          price =price.multiply(BigDecimal.valueOf(1.5)) ;
      }

      return  price ;

    }
}
