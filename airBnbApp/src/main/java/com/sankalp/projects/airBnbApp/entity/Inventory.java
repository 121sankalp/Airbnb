package com.sankalp.projects.airBnbApp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//the tuple of this thing should be unique

@Table(

        uniqueConstraints = @UniqueConstraint
        (name = "unique_hotel_room_date"
                ,columnNames = {"hotel_id","room_id","date"}))
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;


    //one hotel can have many inventory

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id",nullable = false)
    private Hotel hotel ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id",nullable = false)
    private  Room room  ;

    @Column(nullable = false)
    private LocalDate date ;

    @Column(  columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount;

    @Column(  columnDefinition = "INTEGER DEFAULT 0")
    private Integer reservedCount;


    @Column(nullable = false)
    private Integer totalCount ;

    @Column(nullable = false,precision = 5 ,scale = 2)
    private BigDecimal surgeFactor ;

    @Column(nullable = false ,precision = 10 , scale = 2)
    private BigDecimal price ;//base price*surgeFactor

    @Column(nullable = false)
    private String city   ; //we are putting so that we don't have performed join
    // very frequently that will save time

    @Column(nullable = false)
    private  Boolean isClosed ; //we can say that this particular room is not available on particular date


    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private  LocalDateTime createdAt ;

    @Column(nullable = false)
    @UpdateTimestamp
    private  LocalDateTime updatedAt ;
    public Integer getReservedCount() {
        return reservedCount != null ? reservedCount : 0;
    }




}
