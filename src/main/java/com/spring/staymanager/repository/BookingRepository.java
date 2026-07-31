package com.spring.staymanager.repository;

import com.spring.staymanager.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT b FROM Booking b WHERE b.bookingConfirmationCode = :bookingConfirmationCode")
    Optional<Booking> findBookingByBookingConfirmationCode(String bookingConfirmationCode);


}
