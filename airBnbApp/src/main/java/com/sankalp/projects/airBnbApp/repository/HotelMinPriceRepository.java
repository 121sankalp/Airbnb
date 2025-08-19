package com.sankalp.projects.airBnbApp.repository;

import com.sankalp.projects.airBnbApp.entity.Hotel;
import com.sankalp.projects.airBnbApp.entity.HotelMinPrice;
import com.sankalp.projects.airBnbApp.dto.HotelPriceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;

@Repository


public interface HotelMinPriceRepository extends JpaRepository <HotelMinPrice , Long> {

    @Query("""
            SELECT  new com.sankalp.projects.airBnbApp.dto.HotelPriceDto(i.hotel,AVG(i.price))
            FROM HotelMinPrice i
            WHERE i.hotel.city = :city
            AND i.date BETWEEN :startDate AND :endDate
            AND i.hotel.isActive=true
            GROUP BY i.hotel
            """)
    Page<HotelPriceDto> findHotelsWithAvailableInventory(
            @Param("city") String city ,
            @Param("startDate") LocalDate startDate ,
            @Param("endDate") LocalDate endDate  ,
            @Param("roomCount") Integer roomCount ,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
