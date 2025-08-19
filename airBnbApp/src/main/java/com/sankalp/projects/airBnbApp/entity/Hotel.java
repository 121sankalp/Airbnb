package com.sankalp.projects.airBnbApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;

    @Column(nullable = false)
    private String name ;

    @Column(columnDefinition = "TEXT[]")
    private String [] photos ;

    @Column(columnDefinition = "TEXT[]")
    private String [] amenities  ;

    @CreationTimestamp
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private  LocalDateTime updatedAt ;

    @Embedded
    private  HotelContactInfo contactInfo ;

    @Column(nullable = false)
    private Boolean isActive   ; // we don't want reservation for non active hotel

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Room>rooms ;

     @ManyToOne(optional = false,fetch = FetchType.LAZY)
     @JoinColumn(name = "owner_id")
     private User owner ;
    private String city ;




}
