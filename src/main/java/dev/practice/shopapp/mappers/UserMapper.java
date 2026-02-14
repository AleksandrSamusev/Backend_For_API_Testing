package dev.practice.shopapp.mappers;

import dev.practice.shopapp.dto.UserCreateDTO;
import dev.practice.shopapp.models.User;

public class UserMapper {

    public static User toUser(UserCreateDTO dto, Long id) {
        return new User(
                id,
                dto.getFirstName().strip(),
                dto.getLastName().strip(),
                dto.getEmail().strip(),
                dto.getPhoneNumber().strip()
        );
    }
}
