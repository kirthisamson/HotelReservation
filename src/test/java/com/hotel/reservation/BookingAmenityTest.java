package com.hotel.reservation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BookingAmenityTest {

    @Test
    public void createBookingAmenity() {
        Amenity amenity = Amenity.builder()
                .cost(BigDecimal.valueOf(20))
                .limit(2)
                .name("AirCon")
                .build();

        BookingAmenity bookingAmenity = BookingAmenity.builder()
                .amenity(amenity)
                .count(1)
                .build();

        Assert.assertNotNull(bookingAmenity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void countExceedsLimitThrowsIllegalArgumentException() {
        Amenity amenity = Amenity.builder()
                .cost(BigDecimal.valueOf(20))
                .limit(2)
                .name("AirCon")
                .build();

        BookingAmenity bookingAmenity = BookingAmenity.builder()
                .amenity(amenity)
                .count(3)
                .build();
    }


}