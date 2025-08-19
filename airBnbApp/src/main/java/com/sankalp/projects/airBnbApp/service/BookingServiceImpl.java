package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.BookingDto;
import com.sankalp.projects.airBnbApp.dto.BookingRequestDto;
import com.sankalp.projects.airBnbApp.dto.GuestDto;
import com.sankalp.projects.airBnbApp.entity.*;
import com.sankalp.projects.airBnbApp.entity.enums.BookingStatus;
import com.sankalp.projects.airBnbApp.exception.ResourceNotFoundException;
import com.sankalp.projects.airBnbApp.exception.UnauthorizeException;
import com.sankalp.projects.airBnbApp.repository.*;
import com.sankalp.projects.airBnbApp.strategy.PricingService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository ;
    private final HotelRepository hotelRepository ;
    private  final RoomRepository roomRepository ;
    private final InventoryRepository inventoryRepository ;
    private final GuestRepository guestRepository ;
    private final ModelMapper modelMapper ;
    private final  CheckOutService checkOutService ; 
    private  final PricingService pricingService ;
    @Value("${frontend.url}")
    private String frontendUrl ; 

    @Override
    @Transactional


    public BookingDto initializeBooking(BookingRequestDto bookingRequestDto) {
      log.info("initializing booking for hotel room ");
        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId()).orElseThrow(()->
                new ResourceNotFoundException("hotel with {} not found"+ bookingRequestDto.getHotelId())) ;


        Room room = roomRepository.findById(bookingRequestDto.getRoomId()).orElseThrow(()->
                new ResourceNotFoundException("room with {} not found"+ bookingRequestDto.getRoomId())) ;

        // the lock will be released as soon as the transactional method end

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequestDto.getCheckInDate(),
                bookingRequestDto.getCheckOutDate(),bookingRequestDto.getRoomCount()) ;

        long dayCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate())+1;
        if(inventoryList.size()!=dayCount)
        {
            throw  new IllegalStateException("room is not available any more ") ;

        }

        //reserve the room
        for(Inventory inventory :inventoryList)
        {
            inventory.setReservedCount(inventory.getReservedCount()+bookingRequestDto.getRoomCount());
        }



        // will be removed
      inventoryRepository.saveAll(inventoryList) ;
        //creete the booking
         Booking booking = Booking.builder()
                 .bookingStatus(BookingStatus.RESERVED)
                 .hotel(hotel)
                 .checkInDate(bookingRequestDto.getCheckInDate())
                 .checkOutDate(bookingRequestDto.getCheckOutDate())
                 .user(getCurrentUser())
                 .roomCount(bookingRequestDto.getRoomCount())
                 .amount(BigDecimal.valueOf(1000))
                 .updatedAt(LocalDateTime.now())
                 .room(room)
                 .build();
         booking = bookingRepository.save(booking) ;

         return modelMapper.map(booking,BookingDto.class) ;

    }

    @Override
    public BookingDto addGuest(List<GuestDto> guestDto, Long bookingId) {
     Booking booking = bookingRepository.findById(bookingId).orElseThrow(()->
             new ResourceNotFoundException("booking not found with id"+bookingId)) ;

      //we have to check if this booking is expired?

        User user = getCurrentUser() ;

        if(!user.equals(booking.getUser()))
        {
            throw  new UnauthorizeException("booking does not belong to this user with id: "+user.getId()) ;
        }
        if(hasBookingExpired(booking))
        {
            throw  new IllegalStateException("Booking is expired") ;
        }

        if(booking.getBookingStatus()!=BookingStatus.RESERVED)
        {
            throw  new IllegalStateException("booking is not under reserved state,cannot add guest") ;
        }

         for (GuestDto guestdto :guestDto)
         {
             Guest guest = modelMapper.map(guestdto, Guest.class) ;
             guest.setUser(getCurrentUser());
             guest = guestRepository.save(guest) ;
             booking.getGuest().add(guest) ;
         }
         booking.setBookingStatus(BookingStatus.GUEST_ADDED) ;
         bookingRepository.save(booking) ;

    return modelMapper.map(booking , BookingDto.class) ;

    }
   @Transactional
    @Override
    public String initiatePayment(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId).
                orElseThrow(()->new ResourceNotFoundException("booking not found for id "+  bookingId)) ;

        User user = getCurrentUser() ;
        if(!user.equals(booking.getUser()))
        {
            throw  new UnauthorizeException("booking does not belong to this user with id: "+user.getId()) ;
        }

        if(hasBookingExpired(booking))
        {
            throw  new IllegalStateException("Booking is expired") ;
        }



     String sessionUrl =  checkOutService.
          getCheckOutSession(booking,frontendUrl + "/payments/success",
                  frontendUrl + "/payments/failure") ;


     booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);

        bookingRepository.save(booking) ;
        return sessionUrl ;
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) return;

            String sessionId = session.getId();
            Booking booking =
                    bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(() ->
                            new ResourceNotFoundException("Booking not found for session ID: "+sessionId));

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            log.info("Successfully confirmed the booking for Booking ID: {}", booking.getId());
        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }
    }



    public  boolean hasBookingExpired(Booking booking)
    {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());

    }

    public User getCurrentUser()
    {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
    }


}
