package com.hotel.reservation;

import com.hotel.reservation.Utilities.CostCalculator;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class Booking {
    private UUID bookingId;
    private LocalDate startDate;
    private int numberOfDays;
    private User user;
    private BigDecimal totalCost;
    private Room room;
    private List<BookingAmenity> bookingAmenities;

    @Builder
    public Booking(UUID bookingId, LocalDate startDate, int numberOfDays, User user, Room room, List<BookingAmenity> bookingAmenities) {
        this.bookingId = bookingId;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
        this.user = user;
        this.room = room;
        this.bookingAmenities = bookingAmenities;
        checkAmenityCompatibility();
        calculateTotalCost();
        updateRoomAvailablity();
    }

    private void checkAmenityCompatibility() {
        for(Amenity restrictedAmenity: room.getRestrictedAmenities()) {
            for(BookingAmenity bookingAmenity: bookingAmenities) {
                if(bookingAmenity.getAmenity().getName() == restrictedAmenity.getName())
                    throw new IllegalArgumentException("The amenity requested is not available for this room");
            }
        }
    }

    private void calculateTotalCost(){
        totalCost = CostCalculator.calculateTotalCosts(room, bookingAmenities, numberOfDays);
    }

    private void updateRoomAvailablity() {
        room.updateAvailability(startDate, numberOfDays);
    }
}
