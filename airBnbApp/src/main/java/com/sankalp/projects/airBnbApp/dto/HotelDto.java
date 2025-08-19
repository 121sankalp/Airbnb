package com.sankalp.projects.airBnbApp.dto;

import com.sankalp.projects.airBnbApp.entity.HotelContactInfo;
import com.sankalp.projects.airBnbApp.entity.Room;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HotelDto {

    private  Long id ;
    private String name ;
    private String [] photos ;
    private String [] amenities  ;
    private HotelContactInfo contactInfo ;
    private Boolean isActive   ;
    private String city ;

}
