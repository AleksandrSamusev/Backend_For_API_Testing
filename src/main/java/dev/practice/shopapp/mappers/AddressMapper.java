package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.AddressDto;
import dev.practice.shopapp.models.Address;

import java.time.LocalDateTime;

public class AddressMapper {

    public static Address toAddress(AddressDto dto) {
        LocalDateTime now = LocalDateTime.now();
        return new Address(
                dto.getStreetAddress(),
                dto.getApartment() != null ? dto.getApartment() : "",
                dto.getCity(),
                dto.getState() != null ? dto.getState() : "",
                dto.getPostalCode(),
                dto.getCountryCode(),
                now,
                now
        );
    }


}
