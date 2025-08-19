package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.RoomDto;
import com.sankalp.projects.airBnbApp.entity.Room;

import java.util.List;

public interface RoomService {
RoomDto createRoom(RoomDto roomDto,Long hotelId ) ;
List<RoomDto> getAllRoomInHotel (Long hotelId) ;
RoomDto getRoomById(Long roomId) ;
void deleteRoomById (Long roomId)  ;
}
