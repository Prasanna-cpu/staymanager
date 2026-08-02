package com.spring.staymanager.controller;


import com.spring.staymanager.dto.BookingDTO;
import com.spring.staymanager.entity.Booking;
import com.spring.staymanager.response.ApiResponse;
import com.spring.staymanager.service.abstraction.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.catalina.LifecycleState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/save-booking/{roomId}/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> saveBookingHandler(
            @PathVariable String roomId,
            @PathVariable String userId,
            @RequestBody BookingDTO bookingDTO
    ){
        BookingDTO createdBookingDTO = bookingService.saveBooking(roomId, userId, bookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        createdBookingDTO,
                        "Booking Created",
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED
                )
        );
    }

    @GetMapping("/all-bookings")
    public ResponseEntity<ApiResponse> getAllBookingsHandler(){
        List<BookingDTO> bookingDTOS = bookingService.getAllBookings();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        bookingDTOS,
                        "Bookings Retrieved",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/code/{confirmationCode}")
    public ResponseEntity<ApiResponse> getAllBookingsByConfirmationCodeHandler(
            @PathVariable String confirmationCode
    ){
        BookingDTO bookingDTO = bookingService.getBookingsByConfirmationCode(confirmationCode);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        bookingDTO,
                        "Booking retrieved",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

    @DeleteMapping("/bookings/{bookingId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteBookingsHandler(
            @PathVariable String bookingId
    ){
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        null,
                        "Booking cancelled",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

}
