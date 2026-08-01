package com.spring.staymanager.service.abstraction;

import com.spring.staymanager.dto.BookingDTO;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.awt.print.Book;
import java.util.List;

public interface BookingService {

    BookingDTO saveBooking(String roomId, String userId, BookingDTO bookingDTO);

    BookingDTO getBookingsByConfirmationCode(String confirmationCode);

    List<BookingDTO> getAllBookings();

    void cancelBooking(String bookingId);

}
