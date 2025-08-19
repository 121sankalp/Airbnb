package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.entity.Booking;
import com.sankalp.projects.airBnbApp.entity.User;
import com.sankalp.projects.airBnbApp.exception.ResourceNotFoundException;
import com.sankalp.projects.airBnbApp.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor

@Slf4j

@Service

public class CheckOutServiceImpl  implements  CheckOutService{

    private   final  BookingRepository bookingRepository ;


    @Override
    public String getCheckOutSession(Booking booking, String successUrl, String failureUrl) {
        log.info("creating session for booking id {} " , booking.getId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()  ;
        if(!user.equals(booking.getUser()))
        {
            throw new ResourceNotFoundException("user is not permitted ") ;

        }
       try{

         CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                 .setEmail(user.getEmail())
                 .setName(user.getName())
                 .build()  ;

         Customer customer = Customer.create(customerCreateParams)  ;


           SessionCreateParams sessionParams = SessionCreateParams.builder()
                   .setMode(SessionCreateParams.Mode.PAYMENT)
                   .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                   .setCustomer(customer.getId())
                   .setSuccessUrl(successUrl)
                   .setCancelUrl(failureUrl)
                   .addLineItem(
                           SessionCreateParams.LineItem.builder()
                                   .setQuantity(1L)
                                   .setPriceData(
                                           SessionCreateParams.LineItem.PriceData.builder()
                                                   .setCurrency("inr")
                                                   .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                                   .setProductData(
                                                           SessionCreateParams.LineItem.PriceData.ProductData
                                                                   .builder()
                                                                   .setName(booking.getHotel().getName() + " " )
                                                                   .setDescription("booking id : "+booking.getId())
                                                                   .build()
                                                   )
                                                   .build()
                                   )

                                   .build()
                   )

                   .build() ;


           Session  session = Session.create(sessionParams) ;

           booking.setPaymentSessionId(session.getId());

           bookingRepository.save(booking) ;

           log.info("session created successfully for booking id {} " , booking.getId());

           return  session.getUrl() ;


       }
       catch (StripeException e)
       {
           throw  new RuntimeException(e) ;
       }





    }




}
