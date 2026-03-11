package dev.practice.shopapp.services;

import dev.practice.shopapp.dto.AddressDto;
import dev.practice.shopapp.enums.SortingOptions;
import dev.practice.shopapp.dto.UserCreateDto;
import dev.practice.shopapp.dto.UserUpdateDto;
import dev.practice.shopapp.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User createUser(UserCreateDto dto);
    Page<User> getAllUsers(String search, int page, int size, SortingOptions sortBy);
    User getUserById(Long id);
    User updateUser(Long id, UserUpdateDto dto);
    String deleteUser(Long id);

    User addAddress(Long userId, AddressDto dto);
    User removeAddress(Long userId, Long addressId);
    User promoteAddress(Long userId, Long addressId);
}
