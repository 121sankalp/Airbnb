package com.sankalp.projects.airBnbApp.service;

import com.sankalp.projects.airBnbApp.entity.Booking;

public interface CheckOutService {

    String getCheckOutSession(Booking booking , String successUrl , String failureUrl) ;
}
