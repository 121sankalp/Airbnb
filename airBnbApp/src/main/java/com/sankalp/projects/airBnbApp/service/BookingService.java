package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.dto.BookingDto;
import com.sankalp.projects.airBnbApp.dto.BookingRequestDto;
import com.sankalp.projects.airBnbApp.dto.GuestDto;
import com.stripe.model.Event;

import java.util.List;

public interface BookingService    {
    BookingDto initializeBooking(BookingRequestDto bookingRequestDto);

    BookingDto addGuest(List<GuestDto> guestDto, Long bookingId);


    String initiatePayment(Long bookingId);

    void capturePayment(Event event);
}
