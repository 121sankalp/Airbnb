package com.sankalp.projects.airBnbApp.entity;

import com.sankalp.projects.airBnbApp.entity.enums.PaymentStatus;
import com.sankalp.projects.airBnbApp.service.BookingService;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id ;
    @Column(unique = true,nullable = false)
    private String sessionId  ;

    @Enumerated(EnumType.STRING)
    @CollectionTable
    @Column(nullable = false
    )
    private PaymentStatus paymentStatus ;

    @Column(nullable = false, scale = 2 , precision = 10)
    private BigDecimal amount  ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking  booking ;




}
