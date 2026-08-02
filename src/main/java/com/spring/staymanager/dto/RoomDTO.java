package com.spring.staymanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.staymanager.entity.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {

    private String id;

    private String roomType;

    private BigDecimal roomPrice;

    private String roomPhotoUrl;

    private List<BookingDTO> bookings;

    private String roomDescription;

}
