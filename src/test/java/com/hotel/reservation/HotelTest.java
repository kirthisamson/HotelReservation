package com.hotel.reservation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

public class HotelTest {

    Hotel hotel;
    Amenity petAmenity;

    @Before
    public void init() {
        hotel = Hotel.builder()
                .name("California")
                .build();

        petAmenity = Amenity.builder()
                .limit(2)
                .cost(BigDecimal.valueOf(20))
                .name("pet")
                .build();
    }

    @Test
    public void addFloorToHotelTest() {

        List<Amenity> amenitiesForFloor = new ArrayList<Amenity>() {{ add(petAmenity); }};
        HashMap<Integer, Floor> hotelFloors = hotel.addFloor(1,
                                                    true,
                                                            amenitiesForFloor);

        Assert.assertEquals(hotelFloors.size(), 1);
        Assert.assertNotNull(hotelFloors.get(1));
    }

    @Test
    public void addRoomHotelTest() {

        List<Amenity> restrictedAmenitiesForFloor = new ArrayList<Amenity>();
        HashMap<Integer, Floor> hotelFloors = hotel.addFloor(1,
                                                true,
                                                restrictedAmenitiesForFloor);

        Map<Integer, Room> hotelRooms = hotel.addRoom(100,
                                                        hotelFloors.get(1),
                                                        3);

        Assert.assertEquals(hotelRooms.size(), 1);
        Assert.assertNotNull(hotelRooms.get(100));
        assertEquals(hotelRooms.get(100).getFloor(), hotelFloors.get(1));
        assertEquals(hotelRooms.get(100).isHandicapAccessible(),true);
        assertEquals(hotelRooms.get(100).getRestrictedAmenities(), restrictedAmenitiesForFloor);
    }

    @Test
    public void addRoomHotelOnSecondFloorWithRestrictionsTest() {

        List<Amenity> restrictedAmenitiesForFloor = new ArrayList<Amenity>() {{ add(petAmenity); }};
        HashMap<Integer, Floor> hotelFloors = hotel.addFloor(2,
                false,
                restrictedAmenitiesForFloor);

        Map<Integer, Room> hotelRooms = hotel.addRoom(200,
                hotelFloors.get(2),
                3);

        Assert.assertEquals(hotelRooms.size(), 1);
        Assert.assertNotNull(hotelRooms.get(200));
        assertEquals(hotelRooms.get(200).getFloor(), hotelFloors.get(2));
        assertEquals(hotelRooms.get(200).isHandicapAccessible(),false);
        assertEquals(hotelRooms.get(200).getRestrictedAmenities(), restrictedAmenitiesForFloor);
    }

    @Test
    public void makeReservationTest() {

        List<Amenity> restrictedAmenitiesForFloor = new ArrayList<Amenity>();
        HashMap<Integer, Floor> hotelFloors = hotel.addFloor(1,
                                                    true,
                                                                    restrictedAmenitiesForFloor);

        Map<Integer, Room> hotelRooms = hotel.addRoom(100,
                                                        hotelFloors.get(1),
                                                        3);

        BookingAmenity bookingPetAmenity = BookingAmenity.builder()
                                                        .amenity(petAmenity)
                                                        .count(2)
                                                        .build();

        User user = User.builder()
                        .userId(UUID.randomUUID())
                        .firstName("Don")
                        .lastName("Felder")
                        .build();

        Booking booking = hotel.makeReservation(hotelRooms.get(100),
                                LocalDate.now(),
                                3,
                                new ArrayList<BookingAmenity>(){{ add(bookingPetAmenity); }},
                                user);
        assertEquals(1, hotel.getBookings().size());
        assertEquals(new ArrayList<BookingAmenity>(){{ add(bookingPetAmenity); }}, booking.getBookingAmenities());
        assertEquals(3, booking.getNumberOfDays());
        assertEquals(hotelRooms.get(100), booking.getRoom());
        assertEquals(user, booking.getUser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeReservationOnSecondFloorWithRestrictionsThrowsExceptionTest() {

        List<Amenity> restrictedAmenitiesForFloor = new ArrayList<Amenity>() {{ add(petAmenity); }};
        HashMap<Integer, Floor> hotelFloors = hotel.addFloor(2,
                false,
                restrictedAmenitiesForFloor);

        Map<Integer, Room> hotelRooms = hotel.addRoom(200,
                hotelFloors.get(2),
                3);

        BookingAmenity bookingPetAmenity = BookingAmenity.builder()
                                                        .amenity(petAmenity)
                                                        .count(2)
                                                        .build();

        User user = User.builder()
                        .userId(UUID.randomUUID())
                        .firstName("Don")
                        .lastName("Felder")
                        .build();

        Booking booking = hotel.makeReservation(hotelRooms.get(200),
                                                            LocalDate.now(),
                                                            3,
                                                            new ArrayList<BookingAmenity>(){{ add(bookingPetAmenity); }},
                                                            user);
    }

    @Test
    public void roomAvailableIfReservationDoesNotExistsTest() {
        List<Amenity> restrictedAmenitiesForFloor = new ArrayList<Amenity>();
        List<Amenity> amenities = new ArrayList<Amenity>() {{ add(petAmenity); }};
        Map<Integer, Floor> hotelFloors = hotel.addFloor(1,
                                                true,
                                                        restrictedAmenitiesForFloor);

        Map<Integer, Room> hotelRooms = hotel.addRoom(100,
                                                        hotelFloors.get(1),
                                                        3);

        List<Room> avalilableRooms =  hotel.findAvailability(LocalDate.now(), 2, 3,true, amenities);
        assertEquals(1, avalilableRooms.size());
        assertEquals(hotelRooms.get(100), avalilableRooms.get(0));
    }

    @Test
    public void roomNotAvailableIfReservationExistsTest() {
        List<Amenity> restrictedAmenitiesForFloor = new ArrayList<Amenity>();
        List<Amenity> amenities = new ArrayList<Amenity>() {{ add(petAmenity); }};
        HashMap<Integer, Floor> hotelFloors = hotel.addFloor(1,
                true,
                restrictedAmenitiesForFloor);

        Map<Integer, Room> hotelRooms = hotel.addRoom(100,
                hotelFloors.get(1),
                3);

        BookingAmenity bookingPetAmenity = BookingAmenity.builder()
                .amenity(petAmenity)
                .count(2)
                .build();

        User user = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Don")
                .lastName("Felder")
                .build();

        Booking booking = hotel.makeReservation(hotelRooms.get(100),
                LocalDate.now(),
                3,
                new ArrayList<BookingAmenity>(){{ add(bookingPetAmenity); }},
                user);

        List<Room> avalilableRooms =  hotel.findAvailability(LocalDate.now(), 2, 3,false, amenities);
        assertEquals(0, avalilableRooms.size());
    }

}