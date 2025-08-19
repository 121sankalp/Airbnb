package com.sankalp.projects.airBnbApp.dto;
import lombok.Data;
@Data

public class RoomDto {
    private  Long id  ;
    private String type ;
    private String basePrice  ;
    private  String [] photo ;
    private String[] amenities   ;
    private  Integer totalCount ;// number of room of this particular type
    private  Integer capacity ;


}
