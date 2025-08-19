package com.sankalp.projects.airBnbApp.dto;

import com.sankalp.projects.airBnbApp.entity.User;
import com.sankalp.projects.airBnbApp.entity.enums.Gender;

import lombok.Data;

@Data
public class GuestDto {
    private  Long id  ;

    private User user ;


    private String name  ;


    private Gender gender ;

    private  Integer age  ;

}
