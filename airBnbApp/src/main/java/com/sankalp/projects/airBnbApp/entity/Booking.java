package com.sankalp.projects.airBnbApp.entity;

import com.sankalp.projects.airBnbApp.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Booking {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id  ;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "hotel_id")
    private Hotel hotel ;

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room ;

   @Column(nullable = false)
    private Integer roomCount ;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id",nullable = false)
    private  User user ;
   @Column(nullable = false)
   private LocalDate checkInDate  ;

   @Column(nullable = false)
   private LocalDate checkOutDate  ;

   @CreationTimestamp
    private LocalDateTime createdAt ;

   @UpdateTimestamp
    private  LocalDateTime updatedAt ;


   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private BookingStatus bookingStatus ;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "booking_guest" ,
            joinColumns = @JoinColumn(name = "booking_id") ,
            inverseJoinColumns = @JoinColumn(name = "gust_id")
    )

    private Set<Guest> guest  ;

    @Column(nullable = false, scale = 2 , precision = 10)
    private BigDecimal amount  ;

    @Column ( unique = true )

    private String paymentSessionId ;


}
