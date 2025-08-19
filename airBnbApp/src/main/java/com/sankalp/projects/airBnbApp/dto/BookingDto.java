package com.sankalp.projects.airBnbApp.dto;

import com.sankalp.projects.airBnbApp.entity.Hotel;
import com.sankalp.projects.airBnbApp.entity.Room;
import com.sankalp.projects.airBnbApp.entity.User;
import com.sankalp.projects.airBnbApp.entity.enums.BookingStatus;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data


public class BookingDto {
    private  Long id  ;
    private Integer roomCount ;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;


    private LocalDateTime createdAt ;


    private  LocalDateTime updatedAt ;

    private BookingStatus bookingStatus ;

    private Set<GuestDto> guest  ;

}
