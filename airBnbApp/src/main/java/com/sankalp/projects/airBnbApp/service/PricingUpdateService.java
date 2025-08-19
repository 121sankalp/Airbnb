package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.entity.Hotel;
import com.sankalp.projects.airBnbApp.entity.HotelMinPrice;
import com.sankalp.projects.airBnbApp.entity.Inventory;
import com.sankalp.projects.airBnbApp.repository.HotelMinPriceRepository;
import com.sankalp.projects.airBnbApp.repository.HotelRepository;
import com.sankalp.projects.airBnbApp.repository.InventoryRepository;
import com.sankalp.projects.airBnbApp.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service

@RequiredArgsConstructor

@Transactional

public class PricingUpdateService {

//scheduler to update inventory and hotel min price table  every hour

private  final HotelRepository hotelRepository ;

private  final HotelMinPriceRepository hotelMinPriceRepository ;

private  final InventoryRepository inventoryRepository   ;

private  final PricingService pricingService ;

@Scheduled(cron = "0 0 * * * *")
public  void  updatePrice ( )

{
    int page = 0 ;

    int batchSize = 100 ;

    while (true)
    {
        Page<Hotel> hotelPage  = hotelRepository.findAll(PageRequest.of(page ,batchSize)) ;

        if(hotelPage.isEmpty())
        {
            break ;
        }

        hotelPage.getContent().forEach(this::updateHotelPrice);

        page++ ;
    }

}



public   void  updateHotelPrice(Hotel hotel )
{
    log.info("updating hotel price ");
    LocalDate startDate = LocalDate.now() ;
    LocalDate endDate = LocalDate.now() .plusYears(1) ;
    List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate) ;

    updateInventoryPrice(inventoryList) ;
    updateHotelMinPrice( hotel , inventoryList,startDate ,endDate ) ;

}
// find the  cheapest inventory for this  date (start & end )

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        // Compute minimum price per day for the hotel
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        // Prepare HotelPrice entities in bulk
//        List<HotelMinPrice> hotelPrices = new ArrayList<>();
//        dailyMinPrices.forEach((date, price) -> {
//            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
//                    .orElse(new HotelMinPrice(hotel, date));
//            hotelPrice.setMinPrice(price);
//            hotelPrices.add(hotelPrice);
//
//
//
//        });

        List<HotelMinPrice> hotelMinPriceList = new ArrayList<>() ;


        dailyMinPrices.forEach((date,price)->{
            HotelMinPrice hotelMinPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date)
                    .orElse(new HotelMinPrice(hotel ,date)) ;

             hotelMinPrice.setPrice(price); ;
            hotelMinPriceList.add(hotelMinPrice) ;

        });

        // Save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelMinPriceList);



    }


    public void updateInventoryPrice(List<Inventory> inventoryList) {

       for(Inventory inventory :inventoryList)
       {
           BigDecimal price = pricingService.calculateDynamicPricing(inventory) ;
           inventory.setPrice(price);
       }
       inventoryRepository.saveAll(inventoryList) ;
    }

}
