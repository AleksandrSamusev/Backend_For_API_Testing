package dev.practice.shopapp.services;

import dev.practice.shopapp.enums.SortingOptions;
import dev.practice.shopapp.dto.UserCreateDto;
import dev.practice.shopapp.dto.UserUpdateDto;
import dev.practice.shopapp.models.User;

import java.util.List;;

public interface UserService {
    User createUser(UserCreateDto dto);
    List<User> getAllUsers(SortingOptions option);
    User getUserById(Long id);
    User updateUser(Long id, UserUpdateDto dto);
    String deleteUser(Long id);
}
