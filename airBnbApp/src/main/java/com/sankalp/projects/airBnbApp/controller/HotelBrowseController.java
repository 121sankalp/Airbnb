package com.sankalp.projects.airBnbApp.controller;

import com.sankalp.projects.airBnbApp.dto.HotelDto;
import com.sankalp.projects.airBnbApp.dto.HotelInfoDto;
import com.sankalp.projects.airBnbApp.dto.HotelPriceDto;
import com.sankalp.projects.airBnbApp.dto.HotelSearchDto;
import com.sankalp.projects.airBnbApp.service.HotelService;
import com.sankalp.projects.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")

@RequiredArgsConstructor

public class HotelBrowseController {

    private  final InventoryService inventoryService ;
    private final HotelService hotelService ;

  @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>>  searchHotel(@RequestBody HotelSearchDto hotelSearchDto)
  {

     Page<HotelPriceDto>page =  inventoryService.searchHotel(hotelSearchDto) ;

     return  ResponseEntity.ok(page) ;

  }
  @GetMapping("/{hotelId}/info")
    public  ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId)
  {

      return  ResponseEntity.ok(hotelService.getHotelInfoById(hotelId)) ;
  }
}
