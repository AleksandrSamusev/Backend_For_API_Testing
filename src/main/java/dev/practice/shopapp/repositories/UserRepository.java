package dev.practice.shopapp.repositories;

import dev.practice.shopapp.dto.UserCreateDto;
import dev.practice.shopapp.models.User;

import java.util.List;

public interface UserRepository {
    User saveUser(UserCreateDto dro);
    List<User> readAllUsers();
    User getUserById(Long id);
    void rewriteAllUsers(List<User> users);
    void deleteUser(Long userId);
}
