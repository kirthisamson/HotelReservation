package com.hotel.reservation;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Amenity {
    String name;
    int limit;
    BigDecimal cost;
}
