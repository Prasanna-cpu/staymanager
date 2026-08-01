package com.spring.staymanager.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.EmbeddableInstantiator;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings", check = {
        @CheckConstraint(constraint = "check_out_date > check_in_date"),
        @CheckConstraint(constraint = "number_of_adults >= 1"),
        @CheckConstraint(constraint = "number_of_children >= 0")
})
public class Booking extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "total_number_of_guests", nullable = false)
    private Integer totalNumberOfGuests;

    @Column(name = "number_of_children", nullable = false)
    private Integer numberOfChildren = 0;

    @Column(name = "number_of_adults", nullable = false)
    private Integer numberOfAdults = 0;

    @Column(name = "booking_confirmation_code", nullable = false, unique = true)
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalNumberOfGuests(){
        this.totalNumberOfGuests = (this.numberOfAdults == null ? 0 : this.numberOfAdults) + (this.numberOfChildren == null ? 0 : this.numberOfChildren);
    }


    public void setNumberOfAdults(Integer numberOfAdults){
        this.numberOfAdults = numberOfAdults;
        this.calculateTotalNumberOfGuests();
    }

    public void setNumberOfChildren(Integer numberOfChildren){
        this.numberOfChildren = numberOfChildren;
        this.calculateTotalNumberOfGuests();
    }



}
