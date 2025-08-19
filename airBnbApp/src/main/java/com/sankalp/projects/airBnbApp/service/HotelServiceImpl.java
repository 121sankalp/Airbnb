package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.HotelDto;
import com.sankalp.projects.airBnbApp.dto.HotelInfoDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class HotelServiceImpl implements  HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService ;
    private final RoomRepository roomRepository ;

    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("creating new hotel  with hotel dto");
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
        hotel.setOwner(user);
        hotel.setIsActive(false);
        hotelRepository.save(hotel);
        log.info("Creating a new hotel with ID{}", hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);


    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting Hotel with Id {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel Not found with id " + id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
        if(!user.equals(hotel.getOwner()))
            throw  new UnauthorizeException("current user is not the owner") ;

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {

        log.info("updating Hotel with Id {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel Not found with id " + id));
        modelMapper.map(hotelDto, hotel);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;

        if(!user.equals(hotel.getOwner()))
        {
            throw  new UnauthorizeException("user is not owner , cannot do") ;
        }

        hotel.setId(id);

        hotel = hotelRepository.save(hotel);



        return modelMapper.map(hotel, HotelDto.class);

    }

    @Transactional
    @Override
    public void deleteHotelById(Long id) {

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel Not found with id " + id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;

        if(!user.equals(hotel.getOwner()))
        {
            throw  new UnauthorizeException("user is not owner , cannot do") ;
        }



        //delete future inventories for the hotel



        for(Room room :hotel.getRooms())
        {
            inventoryService.deleteAllInventories(room);

            roomRepository.deleteById(room.getId()) ;

        }

        //since room has dependency on hotel like @mapping relation so how you can delete the hotel first.
        //if you hotel then room entity that have dependency on hotel will not be persisted


        hotelRepository.deleteById(id);

     //if you are deleting hotel first then room it still have reference of hotel so not possible

        // you have to go in order of  no dependency like inventory does not have dependency on room / hotel

        //delete the room then delete hotel


    }
  @Transactional
    @Override
    public void activateHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("hotel with {id} doesn't exist" + id)) ;

//      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
//
//      if(!user.equals(hotel.getOwner()))
//      {
//          throw  new UnauthorizeException("user is not owner , cannot do") ;
//      }

        hotel.setIsActive(true);
        hotelRepository.save(hotel) ;
        //create inventory for  all the room for this hotel
        //assuming  only do it once
        for(Room room:hotel.getRooms())
        {
            inventoryService.initializeRoomForYear(room);
        }

    }


    @Transactional
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
         Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->
                 new ResourceNotFoundException("hotel not found with {}"+hotelId)) ;

        List<Room>rooms = hotel.getRooms() ;
        List<RoomDto>roomDtos = rooms.stream().map((x)->
                modelMapper.map(x,RoomDto.class)).toList() ;
         HotelInfoDto hotelInfoDto = new HotelInfoDto() ;
         hotelInfoDto.setHotel(modelMapper.map(hotel,HotelDto.class));
         hotelInfoDto.setRoomDtoList(roomDtos);
         return hotelInfoDto ;
    }
}
