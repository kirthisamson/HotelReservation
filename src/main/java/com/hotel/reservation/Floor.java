package com.hotel.reservation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Floor {
    Integer floorNumber;
    boolean isHandicapAccessible = true;
    List<Amenity> restrictedAmenities;
}
