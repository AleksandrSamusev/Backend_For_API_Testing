package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.AddressDto;
import dev.practice.shopapp.models.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toAddress(AddressDto dto) {
        if(dto == null) {
            return null;
        }
        Address address = new Address();
        address.setStreetAddress(dto.getStreetAddress().strip());
        address.setApartment(dto.getApartment() != null ? dto.getApartment().strip() : "" );
        address.setCity(dto.getCity().strip());
        address.setState(dto.getState() != null ? dto.getState().strip() : "");
        address.setPostalCode(dto.getPostalCode().strip());
        address.setCountryCode(dto.getCountryCode().strip().toUpperCase());
        address.setAddressType(dto.getAddressType());
        return address;
    }
}
