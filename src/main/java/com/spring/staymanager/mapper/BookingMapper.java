package com.spring.staymanager.mapper;

import com.spring.staymanager.dto.BookingDTO;
import com.spring.staymanager.entity.Booking;

public class BookingMapper {

    public static BookingDTO mapToBookingDTO(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumberOfAdults(booking.getNumberOfAdults());
        bookingDTO.setNumberOfChildren(booking.getNumberOfChildren());
        bookingDTO.setTotalNumberOfGuests(booking.getTotalNumberOfGuests());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if(booking.getUser() != null){
            bookingDTO.setUser(UserMapper.mapToUserDTO(booking.getUser()));
        }

        if(booking.getRoom() != null){
            bookingDTO.setRoom(RoomMapper.mapToRoomDTO(booking.getRoom()));
        }

        return bookingDTO;
    }

    public static Booking mapToBooking(BookingDTO bookingDTO){
        Booking booking = new Booking();

        if(bookingDTO.getId() != null){
            booking.setId(bookingDTO.getId());
        }

        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setNumberOfAdults(bookingDTO.getNumberOfAdults());
        booking.setNumberOfChildren(bookingDTO.getNumberOfChildren());
        booking.setTotalNumberOfGuests(bookingDTO.getTotalNumberOfGuests());
        booking.setBookingConfirmationCode(bookingDTO.getBookingConfirmationCode());

        return booking;
    }



}
