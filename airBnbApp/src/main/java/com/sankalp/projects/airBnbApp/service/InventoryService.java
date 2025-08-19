package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.HotelDto;
import com.sankalp.projects.airBnbApp.dto.HotelPriceDto;
import com.sankalp.projects.airBnbApp.dto.HotelSearchDto;
import com.sankalp.projects.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForYear(Room room);
    void deleteAllInventories(Room room) ;

    Page<HotelPriceDto> searchHotel(HotelSearchDto hotelSearchDto);
}
