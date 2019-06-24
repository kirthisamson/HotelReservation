/**
 * ******* BravoLT ******
 *  ******* Hotel Booking System ******
 *  ******* Kirthi Samson Chilkuri ******
 *
 *  This application uses SpringBoot to bootstrap the application and tries to avoid any features for the code itself
 *
 * The Hotel class provides a high level control over the booking process
 * This class provides functionality for adding floors and rooms to the hotel
 * Restrictions can be set at the floor level and are enforced during the booking process
 *
 * Availability of rooms for a certain criteria like number of bedrooms, handicap accessibility
 * and amenities like pets can be searched for using the findAvailability method
 *
 * After selecting the desired room and the amenity count for each amenity a reservation
 * can be made using the makeReservation method, which returns the booking object which
 * contains all the relevant information about the booking including total cost
 *
 * The application is modular and built such that it
 * scales with the business, be it adding another floor, room or amenity or restrictions to amenities
 *
 * Restrictions on amenities can be added and can be flexibly adjusted
 *
 * For time considerations ability to edit a reservation is ommited
 *
 * Unit Tests are included for each relevant class, for time considerations
 * tests do not consider all the edge cases, if any
 *
 * I have tried not to use a database and leverage the framework. Adding Spring @Services and
 * Database can make the code more clean and more beneficial
 *
 * To reduce a lot of boilerplate code like getters and setters, default, allArgs constructors, using the builder pattern,
 * I have used a library called Lombok. Annotations include @Data, @Builder have been used. Please refer documentation at
 * https://projectlombok.org/features/all
 *
 **/

package com.hotel.reservation;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

import static com.hotel.reservation.Utilities.Validatator.*;

@Data
public class Hotel {
    private String name;
    private Map<Integer, Room> rooms;
    private Map<String, List<Amenity>> amenities;
    private Map<UUID, Booking> bookings;
    private HashMap<Integer, Floor> floors;

    @Builder
    public Hotel(String name) {
        this.name = name;
        rooms = new HashMap<Integer, Room>();
        amenities = new HashMap<String, List<Amenity>>();
        bookings = new HashMap<UUID, Booking>();
        floors = new HashMap<Integer, Floor>();
    }

    public HashMap<Integer, Floor> addFloor(int floorNo, boolean handicapAccessible, List<Amenity> restrictedAmenities) {
        validateFloorNo(floorNo);
        validateIshandicapAccessible(handicapAccessible);
        validateAmenitiesList(restrictedAmenities);

        Floor floor =  Floor.builder()
                .floorNumber(floorNo)
                .isHandicapAccessible(handicapAccessible)
                .restrictedAmenities(restrictedAmenities)
                .build();
        floors.put(floor.floorNumber, floor);
        return floors;
    }

    public Map<Integer, Room> addRoom(int roomNo, Floor floor, int numberOfBeds) {
        validateRoomNo(roomNo);
        validateFloor(floor);
        validateNumberOfBeds(numberOfBeds);

        Room room =  Room.builder()
                        .roomNo(roomNo)
                        .numberOfBeds(numberOfBeds)
                        .floor(floor)
                        .build();

        rooms.put(roomNo, room);
        return rooms;
    }

    public List<Room> findAvailability(LocalDate startDate,
                                       int numberOfDays,
                                       int numberOfBeds,
                                       boolean handicapAccessible,
                                       List<Amenity> amenities) {

        validateStartDate(startDate);
        validateNumberOfDays(numberOfDays);
        validateNumberOfBeds(numberOfBeds);
        validateIshandicapAccessible(handicapAccessible);
        validateAmenitiesList(amenities);

        List<Room> availableRooms = new ArrayList<Room>();
        for(Room room: rooms.values()) {
            if(room.isAvailable(startDate, numberOfDays, numberOfBeds, handicapAccessible, amenities)) availableRooms.add(room);
        }
        return availableRooms;
    }

    public Booking makeReservation(Room room,
                                   LocalDate startDate,
                                   int numberOfDays,
                                   List<BookingAmenity> bookingAmenities,
                                   User user) {
        validateRoom(room);
        validateStartDate(startDate);
        validateNumberOfDays(numberOfDays);
        validateBookingAmenitiesList(bookingAmenities);
        validateUser(user);

        if(!room.isAvailableForDays(startDate, numberOfDays)) throw new IllegalArgumentException("Room not available for these dates");

        Booking booking =  Booking.builder()
                        .bookingId(UUID.randomUUID())
                        .startDate(startDate)
                        .numberOfDays(numberOfDays)
                        .room(room)
                        .user(user)
                        .bookingAmenities(bookingAmenities)
                        .build();
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }
}