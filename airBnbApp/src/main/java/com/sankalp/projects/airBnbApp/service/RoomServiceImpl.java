package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.RoomDto;
import com.sankalp.projects.airBnbApp.entity.Hotel;
import com.sankalp.projects.airBnbApp.entity.Room;
import com.sankalp.projects.airBnbApp.entity.User;
import com.sankalp.projects.airBnbApp.exception.ResourceNotFoundException;
import com.sankalp.projects.airBnbApp.exception.UnauthorizeException;
import com.sankalp.projects.airBnbApp.repository.HotelRepository;
import com.sankalp.projects.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j

@Service

public class RoomServiceImpl implements  RoomService{
    private final ModelMapper modelMapper ;
    private final RoomRepository roomRepository ;
    private final HotelRepository hotelRepository ;
    private final InventoryService inventoryService ;
    @Override
    public RoomDto createRoom(RoomDto roomDto,Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->
                new ResourceNotFoundException("hotel not found with {id}"+hotelId)) ;

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;

        if(!user.equals(hotel.getOwner()))
        {
            throw  new UnauthorizeException("user is not owner , cannot do") ;
        }

        Room  room = modelMapper.map(roomDto, Room.class) ;
        room.setHotel(hotel);
        roomRepository.save(room)  ;
        //create an inventory as soon as room is active
        if(hotel.getIsActive())
        {
            inventoryService.initializeRoomForYear(room);
        }

        return modelMapper.map(room , RoomDto.class) ;
    }

    @Override
    public List<RoomDto> getAllRoomInHotel(Long hotelId) {
        log.info("GETTING ALL ROOM IN HOTEL WITH {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->
                new ResourceNotFoundException("hotel not found with {id}"+hotelId)) ;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;

        if(!user.equals(hotel.getOwner()))
        {
            throw  new UnauthorizeException("user is not owner , cannot do") ;
        }

//         return hotel.getRooms().stream().map((element)->modelMapper.map(element,RoomDto.class))
//                 .collect(Collectors.toList());
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : hotel.getRooms()) {
            RoomDto dto = modelMapper.map(room, RoomDto.class);
            roomDtos.add(dto);
        }
        return roomDtos;

    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("getting room with {}",roomId);
        Room room =  roomRepository.findById(roomId).orElseThrow(()->
                new ResourceNotFoundException("room with {} not found"+roomId)) ;


        return modelMapper.map(room,RoomDto.class) ;

    }

    @Transactional//either both of them or none
    @Override
    public void deleteRoomById(Long roomId) {
        log.info("deleting the hotel with {}",roomId) ;

        Room room =  roomRepository.findById(roomId).orElseThrow(()->
                new ResourceNotFoundException("room with {} not found"+roomId)) ;

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;

        if(!user.equals(room.getHotel().getOwner()))
        {
            throw  new UnauthorizeException("user is not owner , cannot do") ;
        }

        //delete all future inventory for this room
        inventoryService.deleteAllInventories(room);

        roomRepository.deleteById(roomId);

    }
}
