package com.hotel.reservation;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.hotel.reservation.Utilities.Validatator.*;

@Data
public class Room {

    private final Map<Integer, BigDecimal> roomPrices = new HashMap<Integer, BigDecimal>() {{
        put(1, new BigDecimal(50));
        put(2, new BigDecimal(75));
        put(3, new BigDecimal(90));
    }};

    private int roomNo;
    private Floor floor;
    private int numberOfBeds;
    private Map<Integer, List<Integer>> availability;

    @Builder
    public Room(int roomNo, Floor floor, int numberOfBeds) {

        this.roomNo = roomNo;
        this.floor = floor;
        this.numberOfBeds = numberOfBeds;
        this.availability = new HashMap<Integer, List<Integer>>();
    }

    public boolean isHandicapAccessible() {
        return floor.isHandicapAccessible;
    }

    public List<Amenity> getRestrictedAmenities() {
        return floor.restrictedAmenities;
    }

    public boolean isAvailable(LocalDate startDate, int numberOfDays, int noOfBeds, boolean handicapAccessible, List<Amenity> amenities) {
        validateStartDate(startDate);
        validateNumberOfDays(numberOfDays);
        validateIshandicapAccessible(handicapAccessible);
        validateAmenitiesList(amenities);

        if(!amenities.isEmpty() && getRestrictedAmenities().containsAll(amenities))
            throw new IllegalArgumentException("Amenities requested are not available for this room");
        return  handicapAccessible == isHandicapAccessible()
                                        && noOfBeds == numberOfBeds
                                        && isAvailableForDays(startDate, numberOfDays);
    }


    // This would be greatly simplified if a database is used as they are more efficient at doing this
    public boolean isAvailableForDays(LocalDate startDate, int numberOfDays) {
        validateStartDate(startDate);
        validateNumberOfDays(numberOfDays);

        int year = startDate.getYear();
        List<Integer> availabilityForYear = availability.get(year);
        if(availabilityForYear == null) {
            availability.put(year, IntStream.rangeClosed(1, 365)
                    .boxed().collect(Collectors.toList()));
            return true;
        }

        int dayOfYear = startDate.getDayOfYear();
        List<Integer> reservationDateRange = IntStream.rangeClosed(dayOfYear, dayOfYear + numberOfDays)
                                                        .boxed().collect(Collectors.toList());
        return availability.get(year).contains(reservationDateRange);
    }

    // This would be greatly simplified if a database is used as they are more efficient at doing this
    public void updateAvailability(LocalDate startDate, int numberOfDays) {
        validateStartDate(startDate);
        validateNumberOfDays(numberOfDays);

        int year = startDate.getYear();
        List<Integer> availabilityForYear = availability.get(year);
        if(availabilityForYear == null) {
            availability.put(year, IntStream.rangeClosed(1, 365)
                    .boxed().collect(Collectors.toList()));
            availabilityForYear = availability.get(year);
        }

        int dayOfYear = startDate.getDayOfYear();
        List<Integer> reservationDateRange = IntStream.rangeClosed(dayOfYear, dayOfYear + numberOfDays)
                                                        .boxed().collect(Collectors.toList());
        availabilityForYear.removeAll(reservationDateRange);
        availability.put(year, availabilityForYear);
    }

    public BigDecimal calculateCost() {
        if(numberOfBeds < 1 || numberOfBeds > roomPrices.size() + 1) throw new IllegalArgumentException("Invalid number of beds");
        return roomPrices.get(numberOfBeds);
    }


}
