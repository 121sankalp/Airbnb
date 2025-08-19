package com.sankalp.projects.airBnbApp.controller;

import com.sankalp.projects.airBnbApp.dto.HotelDto;
import com.sankalp.projects.airBnbApp.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j

public class HotelController {

    private  final  HotelService hotelService ;
    @PostMapping
    public ResponseEntity<HotelDto>createNewHotel(@RequestBody HotelDto hotelDto)
    {
        log.info("attempting to create New Hotel with"+hotelDto.getName()) ;
        HotelDto hotelDto1 = hotelService.createNewHotel(hotelDto) ;
       return  new ResponseEntity<>(hotelDto1 , HttpStatus.CREATED) ;
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity <HotelDto > getHotelById (@PathVariable Long hotelId)
    {
        HotelDto hotelDto = hotelService.getHotelById(hotelId) ;
         return ResponseEntity.ok(hotelDto) ;
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId , @RequestBody HotelDto hotelDto)
    {
        HotelDto hotelDto1 =  hotelService.updateHotelById(hotelId ,  hotelDto)  ;
        return  ResponseEntity.ok(hotelDto1) ;
    }

    @DeleteMapping("/{hotelId}")

    public ResponseEntity<Boolean> deleteHotelById(@PathVariable Long hotelId )
    {
       hotelService.deleteHotelById(hotelId) ;
        return ResponseEntity.noContent().build() ;
    }

    @PatchMapping("/{hotelId}")

    public ResponseEntity<Boolean> activateHotelById(@PathVariable Long hotelId)
    {
         hotelService.activateHotel(hotelId);

         return  ResponseEntity.noContent().build() ;
    }









}
