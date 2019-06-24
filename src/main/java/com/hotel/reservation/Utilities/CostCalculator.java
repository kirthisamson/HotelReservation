package com.hotel.reservation.Utilities;

import com.hotel.reservation.BookingAmenity;
import com.hotel.reservation.Room;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CostCalculator {

    public static BigDecimal calculateTotalCosts(Room room, List<BookingAmenity> bookingAmenities, int numberOfDays) {
        if(numberOfDays < 0) new IllegalAccessException("Invalid Number of days");
        return getRoomCosts(room, numberOfDays).add(getAmenityCosts(bookingAmenities, numberOfDays));
    }

    public static BigDecimal getRoomCosts(Room room, int numberOfDays) {
        if(numberOfDays < 0) new IllegalAccessException("Invalid Number of days");
        return room.calculateCost().multiply(new BigDecimal(numberOfDays));
    }

    public static BigDecimal getAmenityCosts(List<BookingAmenity> bookingAmenities, int numberOfDays) {
        if(bookingAmenities.size() <= 0) return BigDecimal.valueOf(0);
        if(numberOfDays < 0) new IllegalAccessException("Invalid Number of days");
        BigDecimal amenityCosts = new BigDecimal(0);
        for(BookingAmenity bookingAmenity : bookingAmenities) amenityCosts.add(bookingAmenity.getAmenity().getCost());
        return amenityCosts.multiply(new BigDecimal(numberOfDays));
    }
}
