package com.sankalp.projects.airBnbApp.entity;

import com.sankalp.projects.airBnbApp.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table
public class Guest {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id  ;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user ;

    @Column(nullable = false)
    private String name  ;

    @Enumerated(EnumType.STRING)
    private Gender gender ;

    private  Integer age  ;

    // one booking can have many guest
    //one guest can have many booking







}
