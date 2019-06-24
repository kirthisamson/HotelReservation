package com.hotel.reservation.Utilities;

import com.hotel.reservation.*;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class Validatator {

    public static void validateStartDate(LocalDate startDate) {
        assertNotNull(startDate);
        if(startDate.isBefore(LocalDate.now())) throw new IllegalArgumentException("Date cannot be in the past");
    }

    public static void validateNumberOfDays(int numberOfDays) {
        assertNotNull(numberOfDays);
        if(numberOfDays < 0) throw new IllegalArgumentException("Invalid number of days");
    }

    public static void validateIshandicapAccessible(boolean isHandicapAccessible) {
        assertNotNull(isHandicapAccessible);
    }

    public static void validateAmenitiesList(List<Amenity> amenities) {
        assertNotNull(amenities);
    }

    public static void validateBookingAmenitiesList(List<BookingAmenity> bookingAmenities) {
        assertNotNull(bookingAmenities);
    }

    public static void validateUser(User user) {
        assertNotNull(user);
    }

    public static void validateFloorNo(int floorNo) {
        assertNotNull(floorNo);
        if(floorNo < 0) throw new IllegalArgumentException("Invalid number for a floor");
    }

    public static void validateRoomNo(int roomNo) {
        assertNotNull(roomNo);
        if(roomNo < 0) throw new IllegalArgumentException("Invalid number of rooms");
    }

    public static void validateNumberOfBeds(int numberOfBeds) {
        assertNotNull(numberOfBeds);
        if(numberOfBeds < 0) throw new IllegalArgumentException("Invalid number for beds");
    }

    public static void validateFloor(Floor floor) {
        assertNotNull(floor);
        if(floor.getFloorNumber() < 0) throw new IllegalArgumentException("Invalid floor");
    }

    public static void validateRoom(Room room) {
        assertNotNull(room);
        if(room.getRoomNo() < 0) throw new IllegalArgumentException("Invalid room");
    }

}
