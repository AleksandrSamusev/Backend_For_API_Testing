package dev.practice.shopapp.services;

import dev.practice.shopapp.SortingOptions;
import dev.practice.shopapp.dto.UserCreateDTO;
import dev.practice.shopapp.dto.UserUpdateDTO;
import dev.practice.shopapp.exceptions.ResourceNotFoundException;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository = new UserRepository();

    public User createUser(UserCreateDTO dto) {
        return userRepository.saveUser(dto);
    }

    public List<User> getAllUsers(Optional<SortingOptions> option) {
        List<User> users = userRepository.readAllUsers();
        if (option.isPresent()) {
            if (option.get() == SortingOptions.FIRST_NAME_ASC) {
                users.sort(Comparator.comparing(User::getFirstName));
            } else if (option.get() == SortingOptions.FIRST_NAME_DESC) {
                users.sort(Comparator.comparing(User::getFirstName).reversed());
            } else if (option.get() == SortingOptions.LAST_NAME_ASC) {
                users.sort(Comparator.comparing(User::getLastName));
            } else if (option.get() == SortingOptions.LAST_NAME_DESC) {
                users.sort(Comparator.comparing(User::getLastName).reversed());
            } else if (option.get() == SortingOptions.ID_DESC) {
                users.sort(Comparator.comparing(User::getId).reversed());
            }
        }
        return users;
    }

    public User getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User updateUser(Long userId, UserUpdateDTO dto) {

        List<User> users = userRepository.readAllUsers();
        User targetUser = users.stream().filter(u -> Objects.equals(u.getId(), userId))
                .findAny().orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with given ID: " + userId));
        if (dto.getFirstName() != null) {
            targetUser.setFirstName(dto.getFirstName().strip());
        }
        if (dto.getLastName() != null) {
            targetUser.setLastName(dto.getLastName().strip());
        }
        if (dto.getEmail() != null) {
            targetUser.setEmail(dto.getEmail().strip());
        }
        if (dto.getPhoneNumber() != null) {
            targetUser.setPhoneNumber(dto.getPhoneNumber().strip());
        }
        userRepository.rewriteAllUsers(users);
        return targetUser;
    }

    public String deleteUser(Long userId) {
        userRepository.deleteUser(userId);
        return "User with ID: " + userId + " successfully deleted";
    }
}
