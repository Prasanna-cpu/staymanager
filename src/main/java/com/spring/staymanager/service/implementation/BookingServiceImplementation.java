package com.spring.staymanager.service.implementation;

import com.spring.staymanager.dto.BookingDTO;
import com.spring.staymanager.entity.Booking;
import com.spring.staymanager.entity.Room;
import com.spring.staymanager.entity.User;
import com.spring.staymanager.exception.BadRequestException;
import com.spring.staymanager.exception.ObjectNotFoundException;
import com.spring.staymanager.mapper.BookingMapper;
import com.spring.staymanager.repository.BookingRepository;
import com.spring.staymanager.repository.RoomRepository;
import com.spring.staymanager.repository.UserRepository;
import com.spring.staymanager.service.abstraction.BookingService;
import com.spring.staymanager.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.List;


@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImplementation implements BookingService  {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private boolean roomIsAvailable(BookingDTO bookingRequest, List<Booking> existingBookings){
        return existingBookings
                .stream()
                .noneMatch(
                        existingBooking ->
                                bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))

                );

    }

    @Override
    public BookingDTO saveBooking(String roomId, String userId, BookingDTO bookingDTO) {
        if(bookingDTO.getCheckOutDate().isBefore(bookingDTO.getCheckInDate())){
            throw new IllegalArgumentException("Check-out date cannot be before check-in date");
        }
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ObjectNotFoundException("Room not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        List<Booking> existingBookings = room.getBookings();

        if(!roomIsAvailable(bookingDTO, existingBookings)){
            throw new BadRequestException("Room is not available for the requested dates");
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setRoom(room);

        String confirmationCode = Utils.generateRandomConfirmationCode(6);
        booking.setBookingConfirmationCode(confirmationCode);
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setNumberOfAdults(bookingDTO.getNumberOfAdults());
        booking.setNumberOfChildren(bookingDTO.getNumberOfChildren());
        booking.setTotalNumberOfGuests(bookingDTO.getTotalNumberOfGuests());

        Booking savedBooking = bookingRepository.save(booking);
        BookingDTO savedBookingDTO = BookingMapper.mapToBookingDTO(savedBooking);
        return savedBookingDTO;

    }

    @Override
    public BookingDTO getBookingsByConfirmationCode(String confirmationCode) {
        Booking booking = bookingRepository.findBookingByBookingConfirmationCode(confirmationCode).orElseThrow(
                () -> new ObjectNotFoundException("Booking not found")
        );
        BookingDTO bookingDTO = BookingMapper.mapToBookingDTO(booking);
        return bookingDTO;
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> bookingDTOS = bookings.stream().map(BookingMapper::mapToBookingDTO).toList();
        return bookingDTOS;
    }

    @Override
    public void cancelBooking(String bookingId) {
        bookingRepository.findById(bookingId).ifPresentOrElse(bookingRepository::delete,
                () -> {
                    throw new ObjectNotFoundException("Booking not found");
                });
    }
}
