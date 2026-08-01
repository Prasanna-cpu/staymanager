package com.spring.staymanager.repository;

import com.spring.staymanager.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT b FROM Booking b WHERE b.bookingConfirmationCode = :bookingConfirmationCode")
    Optional<Booking> findBookingByBookingConfirmationCode(String bookingConfirmationCode);


    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId")
    List<Booking>  getBookingsByRoomId(String roomId);


    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(String userId);


}
