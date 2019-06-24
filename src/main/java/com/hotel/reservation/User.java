package com.hotel.reservation;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class User {
    UUID userId;
    String firstName;
    String lastName;
}