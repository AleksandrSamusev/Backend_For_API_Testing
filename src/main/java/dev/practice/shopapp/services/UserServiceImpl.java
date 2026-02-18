package dev.practice.shopapp.services;

import dev.practice.shopapp.SortingOptions;
import dev.practice.shopapp.dto.UserCreateDto;
import dev.practice.shopapp.dto.UserUpdateDto;
import dev.practice.shopapp.exceptions.ResourceNotFoundException;
import dev.practice.shopapp.models.Address;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository = new UserRepository();

    public User createUser(UserCreateDto dto) {
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

    public User updateUser(Long userId, UserUpdateDto dto) {
        List<User> users = userRepository.readAllUsers();

        // 1. Find the target user
        User targetUser = users.stream()
                .filter(u -> Objects.equals(u.getId(), userId))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given ID: " + userId));

        // 2. Add a check: If email is being updated, check if it's taken by ANOTHER user
        if (dto.getEmail() != null) {
            String newEmail = dto.getEmail().strip().toLowerCase();

            boolean emailTakenBySomeoneElse = users.stream()
                    .anyMatch(u -> !Objects.equals(u.getId(), userId) && // NOT this user
                            u.getEmail().equalsIgnoreCase(newEmail)); // BUT matches this email

            if (emailTakenBySomeoneElse) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }

            targetUser.setEmail(newEmail);
        }

        // 3. Update other fields
        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            targetUser.setFirstName(dto.getFirstName().strip());
        }
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            targetUser.setLastName(dto.getLastName().strip());
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            targetUser.setPhoneNumber(dto.getPhoneNumber().strip());
        }
        Address address = targetUser.getAddress();
        if (address == null) {
            address = new Address();
            targetUser.setAddress(address);
        }
        if (dto.getAddress() != null) {
            if (dto.getAddress().getStreetAddress() != null && !dto.getAddress().getStreetAddress().isBlank()) {
                address.setStreetAddress(dto.getAddress().getStreetAddress());
            }
            if (dto.getAddress().getApartment() != null && !dto.getAddress().getApartment().isBlank()) {
                address.setApartment(dto.getAddress().getApartment());
            }
            if (dto.getAddress().getCity() != null && !dto.getAddress().getCity().isBlank()) {
                address.setCity(dto.getAddress().getCity());
            }
            if (dto.getAddress().getState() != null && !dto.getAddress().getState().isBlank()) {
                address.setState(dto.getAddress().getState());
            }
            if (dto.getAddress().getPostalCode() != null && !dto.getAddress().getPostalCode().isBlank()) {
                address.setPostalCode(dto.getAddress().getPostalCode());
            }
            if (dto.getAddress().getCountryCode() != null && !dto.getAddress().getCountryCode().isBlank()) {
                address.setCountryCode(dto.getAddress().getCountryCode());
            }
        }
        userRepository.rewriteAllUsers(users);
        return targetUser;
    }

    public String deleteUser(Long userId) {
        userRepository.deleteUser(userId);
        return "User with ID: " + userId + " successfully deleted";
    }
}
