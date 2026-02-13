package dev.practice.shopapp.services;

import dev.practice.shopapp.SortingOptions;
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

    public User createUser(User user) {
        return userRepository.saveUser(user);

    }

    public List<User> getAllUsers(Optional<SortingOptions> option) {
        List<User> users =  userRepository.readAllUsers();
        if(option.isPresent()) {
            if(option.get() == SortingOptions.FIRST_NAME_ASC) {
                users.sort(Comparator.comparing(User::getFirstName));
            } else if(option.get() == SortingOptions.FIRST_NAME_DESC) {
                users.sort(Comparator.comparing(User::getFirstName).reversed());
            } else if(option.get() == SortingOptions.LAST_NAME_ASC) {
                users.sort(Comparator.comparing(User::getLastName));
            } else if(option.get() == SortingOptions.LAST_NAME_DESC) {
                users.sort(Comparator.comparing(User::getLastName).reversed());
            } else if(option.get() == SortingOptions.ID_DESC) {
                users.sort(Comparator.comparing(User::getId).reversed());
            }
        }
        return users;
    }

    public User getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User updateUser(Long userId, User user) {

        List<User> users = userRepository.readAllUsers();
        User targetUser = users.stream().filter(u -> Objects.equals(u.getId(), userId))
                .findAny().orElseThrow(() -> new ResourceNotFoundException("User not found with given ID"));
        if(!user.getFirstName().isBlank() & !user.getFirstName().isEmpty()) {
            targetUser.setFirstName(user.getFirstName().strip());
        }
        if(!user.getLastName().isBlank() & !user.getLastName().isEmpty()) {
            targetUser.setLastName(user.getLastName().strip());
        }
        if(!user.getEmail().isBlank() & !user.getEmail().isEmpty()) {
            targetUser.setEmail(user.getEmail().strip());
        }
        if(!user.getPhoneNumber().isBlank() & !user.getPhoneNumber().isEmpty()) {
            targetUser.setPhoneNumber(user.getPhoneNumber().strip());
        }
        userRepository.rewriteAllUsers(users);
        return targetUser;
    }

    public String deleteUser(Long userId) {
        userRepository.deleteUser(userId);
        return "User with ID: " + userId + " successfully deleted";
    }
}
