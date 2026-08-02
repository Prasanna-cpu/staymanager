package com.spring.staymanager.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    public String id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkInDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkOutDate;

    private Integer totalNumberOfGuests;

    private Integer numberOfChildren;

    private Integer numberOfAdults;

    private String bookingConfirmationCode;

    private UserDTO user;

    private RoomDTO room;

}
