package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.HotelDto;
import com.sankalp.projects.airBnbApp.dto.HotelInfoDto;
import com.sankalp.projects.airBnbApp.entity.Hotel;
import lombok.Lombok;

public interface HotelService {

    public  HotelDto createNewHotel(HotelDto hotelDto) ;
    public  HotelDto getHotelById(Long id) ;
    public  HotelDto updateHotelById(Long id ,HotelDto hotelDto) ;
    public  void  deleteHotelById(Long id) ;
    public void activateHotel(Long id) ;

    HotelInfoDto getHotelInfoById(Long hotelId);
}
