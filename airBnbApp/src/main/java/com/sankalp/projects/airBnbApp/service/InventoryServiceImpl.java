package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.HotelDto;
import com.sankalp.projects.airBnbApp.dto.HotelPriceDto;
import com.sankalp.projects.airBnbApp.dto.HotelSearchDto;
import com.sankalp.projects.airBnbApp.entity.Hotel;
import com.sankalp.projects.airBnbApp.entity.Inventory;
import com.sankalp.projects.airBnbApp.entity.Room;
import com.sankalp.projects.airBnbApp.repository.HotelMinPriceRepository;
import com.sankalp.projects.airBnbApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class InventoryServiceImpl implements  InventoryService{

  private final InventoryRepository inventoryRepository ;
  private final ModelMapper modelMapper ;
  private final HotelMinPriceRepository hotelMinPriceRepository ;
    @Override
    public void initializeRoomForYear(Room room) {
        LocalDate today = LocalDate.now() ;
        LocalDate  end = today.plusYears(1) ;
        for (; !today.isAfter(end); today = today.plusDays(1))
        {
            Inventory  inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .isClosed(false)
                    .reservedCount(0)
                    .build();
            inventoryRepository.save(inventory) ;
        }

    }

    @Override
    public void deleteAllInventories(Room room) {
       inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotel(HotelSearchDto hotelSearchDto) {

        Pageable pageable = PageRequest.of(hotelSearchDto.getPage(),hotelSearchDto.getSize()) ;
        Long dateCount = ChronoUnit.DAYS.between(hotelSearchDto.getStartDate(),
                hotelSearchDto.getEndDate())+1;
//        Page<Hotel> hotelPage=  inventoryRepository.findHotelWithAvailableInventory(hotelSearchDto.getCity(),
//                hotelSearchDto.getStartDate(),
//                hotelSearchDto.getEndDate(),
//                hotelSearchDto.getRoomCount(),
//                dateCount,
//                pageable
//
//        );

//         business logic - 90 days
        Page<HotelPriceDto> hotelPage =
                hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchDto.getCity(),
                        hotelSearchDto.getStartDate(), hotelSearchDto.getEndDate(), hotelSearchDto.getRoomCount(),
                        dateCount, pageable);

        return hotelPage;


        //dateCount is number of day between start date and end date
//
    //   return   hotelPage.map((element)->modelMapper.map(element,HotelPriceDto.class)) ;



    }
}
