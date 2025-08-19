package com.sankalp.projects.airBnbApp.controller;

import com.sankalp.projects.airBnbApp.dto.BookingDto;
import com.sankalp.projects.airBnbApp.dto.BookingRequestDto;
import com.sankalp.projects.airBnbApp.dto.GuestDto;
import com.sankalp.projects.airBnbApp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {
private final BookingService bookingService;
    @PostMapping("/init")
  public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequestDto bookingRequestDto)
  {
      return ResponseEntity.ok(bookingService.initializeBooking(bookingRequestDto));
  }

  @PostMapping("/{bookingId}/addGuest")
    public  ResponseEntity<BookingDto>addGuest(@RequestBody List<GuestDto>guestDto , @PathVariable Long bookingId )
  {
      return  ResponseEntity.ok(bookingService.addGuest(guestDto,bookingId)) ;
  }

    @PostMapping("/{bookingId}/payments")
    public  ResponseEntity<Map<String , String>> initiatePayment(@PathVariable Long bookingId )

    {
        String sessionUrl = bookingService . initiatePayment (bookingId) ;


         return  ResponseEntity.ok(Map.of("SessionUrl",sessionUrl)) ;

    }






}
