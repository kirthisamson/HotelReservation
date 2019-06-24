package com.hotel.reservation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingAmenity {
    private Amenity amenity;
    private int count;

    public BookingAmenity(Amenity amenity, int count) {
        this.amenity = amenity;
        if(count > amenity.limit) throw new IllegalArgumentException("Amenity count exceeds limit");
        this.count = count;
    }
}
