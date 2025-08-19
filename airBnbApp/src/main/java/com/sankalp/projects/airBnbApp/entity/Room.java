package com.sankalp.projects.airBnbApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id  ;


    @ManyToOne(fetch = FetchType.LAZY)//many room can belong to one hotel
    @JoinColumn(name = "hotel_id",nullable = false)
    private Hotel hotel ;
    //when you create a room you have to specify the hotel

    @Column(nullable = false) // which type of room weather it is  normal
    private String type ;

    @Column(nullable = false,precision = 10 ,scale = 2)
    private BigDecimal basePrice  ;

    @Column(columnDefinition = "TEXT[]")
    private  String [] photo ;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities   ;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private LocalDateTime updatedAt ;

    @Column(nullable = false)
    private  Integer totalCount ;// number of room of this particular type

    @Column(nullable = false)
    private  Integer capacity ;


}
