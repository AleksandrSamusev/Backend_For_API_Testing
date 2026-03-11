package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.UserCreateDto;
import dev.practice.shopapp.models.Address;
import dev.practice.shopapp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final AddressMapper addressMapper;

    public User toUser(UserCreateDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getAddresses() != null) {
            dto.getAddresses().forEach(addressDto -> {
                Address address = addressMapper.toAddress(addressDto);
                user.addAddress(address);
            });
        }
        return user;
    }
}
