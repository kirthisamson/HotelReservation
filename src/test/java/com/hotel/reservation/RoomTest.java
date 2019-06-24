package com.hotel.reservation;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class RoomTest {
    Floor firstFloor, secondFloor, thirdFloor;
    Amenity petAmenity;

    @Before
    public void init() {
        petAmenity = Amenity.builder()
                .limit(2)
                .cost(BigDecimal.valueOf(20))
                .name("pet")
                .build();

        firstFloor = Floor.builder()
                .floorNumber(1)
                .isHandicapAccessible(true)
                .restrictedAmenities(new ArrayList<Amenity>())
                .build();

        secondFloor = Floor.builder()
                .floorNumber(2)
                .isHandicapAccessible(false)
                .restrictedAmenities(new ArrayList<Amenity>() {{ add(petAmenity); }})
                .build();

        thirdFloor = Floor.builder()
                .floorNumber(3)
                .isHandicapAccessible(false)
                .restrictedAmenities(new ArrayList<Amenity>() {{ add(petAmenity); }})
                .build();
    }

    @Test
    public void createRoomTest() {
        Room roomOnFirstFloor = Room.builder()
                .floor(firstFloor)
                .roomNo(100)
                .numberOfBeds(2)
                .build();

        assertNotNull(roomOnFirstFloor);
        assertEquals(new HashMap<Integer, List<Integer>>(), roomOnFirstFloor.getAvailability());
    }

    @Test
    public void oneBedroomCostCalculationTest() {
        Room roomOnFirstFloor = Room.builder()
                                    .floor(firstFloor)
                                    .roomNo(100)
                                    .numberOfBeds(1)
                                    .build();

        assertEquals(BigDecimal.valueOf(50), roomOnFirstFloor.calculateCost());
    }

    @Test
    public void twoBedroomCostCalculationTest() {
        Room roomOnFirstFloor = Room.builder()
                                    .floor(firstFloor)
                                    .roomNo(100)
                                    .numberOfBeds(2)
                                    .build();

        assertEquals(BigDecimal.valueOf(75), roomOnFirstFloor.calculateCost());
    }

    @Test
    public void threeBedroomCostCalculationTest() {
        Room roomOnFirstFloor = Room.builder()
                .floor(firstFloor)
                .roomNo(100)
                .numberOfBeds(3)
                .build();

        assertEquals(BigDecimal.valueOf(90), roomOnFirstFloor.calculateCost());
    }

    @Test
    public void secondFloorCantBeHandicapAccessible() {
        Room roomOnSecondFloor = Room.builder()
                .floor(secondFloor)
                .roomNo(200)
                .numberOfBeds(2)
                .build();

        assertFalse(roomOnSecondFloor.isHandicapAccessible());
    }

    @Test
    public void thirdFloorCantBeHandicapAccessible() {
        Room roomOnThirdFloor = Room.builder()
                .floor(thirdFloor)
                .roomNo(300)
                .numberOfBeds(2)
                .build();

        assertFalse(roomOnThirdFloor.isHandicapAccessible());
    }

    @Test
    public void secondFloorCantAcceptPets() {
        Room roomOnSecondFloor = Room.builder()
                .floor(secondFloor)
                .roomNo(200)
                .numberOfBeds(2)
                .build();
        assertEquals(new ArrayList<Amenity>() {{ add(petAmenity); }},
                roomOnSecondFloor.getRestrictedAmenities());
    }

    @Test
    public void thirdFloorCantAcceptPets() {
        Room roomOnThirdFloor = Room.builder()
                .floor(thirdFloor)
                .roomNo(300)
                .numberOfBeds(2)
                .build();
        assertEquals(new ArrayList<Amenity>() {{ add(petAmenity); }},
                roomOnThirdFloor.getRestrictedAmenities());
    }

    @Test
    public void checkAvailabilityForRoom() {
        LocalDate today = LocalDate.now();
        Room roomOnSecondFloor = Room.builder()
                .floor(secondFloor)
                .roomNo(200)
                .numberOfBeds(2)
                .build();

        assertTrue(roomOnSecondFloor.isAvailable(today, 4, 2, false, new ArrayList<Amenity>()));
    }

    @Test
    public void checkAvailabilityForRoomAfterUpdate() {
        LocalDate today = LocalDate.now();
        Room roomOnSecondFloor = Room.builder()
                .floor(secondFloor)
                .roomNo(200)
                .numberOfBeds(2)
                .build();

        roomOnSecondFloor.updateAvailability(today, 4);
        assertFalse(roomOnSecondFloor.isAvailable(today, 4, 2, false, new ArrayList<Amenity>()));
    }

    @Test
    public void updateAvailabilityForRoom() {
        LocalDate today = LocalDate.now();
        Room roomOnSecondFloor = Room.builder()
                .floor(secondFloor)
                .roomNo(200)
                .numberOfBeds(2)
                .build();

        roomOnSecondFloor.updateAvailability(today, 4);

        List<Integer> availabilityForYear = roomOnSecondFloor.getAvailability().get(today.getYear());
        List<Integer> bookingDateRange = IntStream.rangeClosed(today.getYear(), today.getYear() + 5)
                .boxed().collect(Collectors.toList());

        assertFalse(availabilityForYear.contains(bookingDateRange));
    }

}