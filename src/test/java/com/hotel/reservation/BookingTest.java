package com.hotel.reservation;

import com.hotel.reservation.Utilities.CostCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "com.hotel.reservation.*")
public class BookingTest {

    Booking booking;

    Floor firstFloor;
    Floor secondFloor;

    Room oneBedroomOnFirstFloor;

    Room oneBedroomOnSecondFloor;

    User user;
    Amenity petAmenity;
    BookingAmenity bookingPetAmenity_count2;

    LocalDate startDate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        startDate = LocalDate.now();

        petAmenity = Amenity.builder()
                .name("pet")
                .limit(2)
                .build();

        bookingPetAmenity_count2 = BookingAmenity.builder()
                .amenity(petAmenity)
                .count(2)
                .build();

        firstFloor = Floor.builder()
                .floorNumber(1)
                .isHandicapAccessible(true)
                .restrictedAmenities(new ArrayList<>())
                .build();

        oneBedroomOnFirstFloor = Room.builder()
                .roomNo(100)
                .numberOfBeds(1)
                .floor(firstFloor)
                .build();

        oneBedroomOnSecondFloor = Room.builder()
                .roomNo(100)
                .numberOfBeds(1)
                .floor(firstFloor)
                .build();

        user = User.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    public void totalCalculatedCost_2DaysNoAmenitiesTest() {

        List<BookingAmenity> bookingAmenities = new ArrayList<BookingAmenity>();
        PowerMockito.mockStatic(CostCalculator.class);
        PowerMockito.when(CostCalculator.calculateTotalCosts(oneBedroomOnFirstFloor, bookingAmenities, 2)).thenReturn(BigDecimal.valueOf(100));

        booking = Booking.builder()
                .bookingId(UUID.randomUUID())
                .numberOfDays(2)
                .startDate(LocalDate.now())
                .room(oneBedroomOnFirstFloor)
                .user(user)
                .bookingAmenities(bookingAmenities)
                .build();

        assertEquals(BigDecimal.valueOf(100), booking.getTotalCost());
    }

    @Test (expected = IllegalArgumentException.class)
    public void petsOnSecondFloorThrowsException() {

        Floor secondFloorWithPets = Floor.builder()
                .floorNumber(2)
                .isHandicapAccessible(false)
                .restrictedAmenities(new ArrayList<Amenity>() { { add(petAmenity); } })
                .build();

        oneBedroomOnSecondFloor = Room.builder()
                .roomNo(200)
                .numberOfBeds(1)
                .floor(secondFloorWithPets)
                .build();

        List<BookingAmenity> bookingAmenities = new ArrayList<BookingAmenity>() {{}};
        bookingAmenities.add(bookingPetAmenity_count2);

        booking = Booking.builder()
                .bookingId(UUID.randomUUID())
                .numberOfDays(2)
                .startDate(LocalDate.now())
                .room(oneBedroomOnSecondFloor)
                .user(user)
                .bookingAmenities(bookingAmenities)
                .build();
    }

}