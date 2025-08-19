package com.sankalp.projects.airBnbApp.controller;

import com.sankalp.projects.airBnbApp.dto.RoomDto;
import com.sankalp.projects.airBnbApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/hotels/{hotelId}/rooms")
public class  RoomAdminController {
    private  final  RoomService roomService ;
    @PostMapping
    ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto, @PathVariable  Long hotelId)
    {
        RoomDto room = roomService.createRoom(roomDto,hotelId) ;

        return  new ResponseEntity<>(room , HttpStatus.CREATED) ;

    }

    @GetMapping
    ResponseEntity<List<RoomDto>> getAllRoomInHotel(@PathVariable Long hotelId)
    {
        List<RoomDto>roomDto = roomService.getAllRoomInHotel(hotelId) ;
        return ResponseEntity.ok(roomDto) ;
    }

    @GetMapping("/{roomId}")
    ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId)
    {

        return ResponseEntity.ok(roomService.getRoomById(roomId)) ;
    }

    @DeleteMapping("/{roomId}")

    public ResponseEntity<Boolean> deleteRoomById(@PathVariable Long roomId)
    {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build() ;

    }







}
