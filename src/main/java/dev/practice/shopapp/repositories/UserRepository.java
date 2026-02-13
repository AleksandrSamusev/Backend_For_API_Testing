package dev.practice.shopapp.repositories;

import dev.practice.shopapp.exceptions.ResourceNotFoundException;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class UserRepository {

    private static final Path filePath = Path.of("users.txt");

    public User saveUser(User user) {
        user.setId(Utils.generateId());
        user.setFirstName(user.getFirstName().strip());
        user.setLastName(user.getLastName().strip());
        user.setEmail(user.getEmail().strip());
        user.setPhoneNumber(user.getPhoneNumber().strip());
        String userAsString = Utils.createUserString(user);
        try {
            List<String> lines = Files.readAllLines(filePath);
            lines.add(userAsString);
            Files.write(filePath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> readAllUsers() {
        return Utils.getAllUsers();
    }

    public User getUserById(Long userId) {
        List<User> users = Utils.getAllUsers();
        return users.stream().filter(user -> Objects.equals(user.getId(), userId))
                .findAny()
                .orElseThrow(()->new ResourceNotFoundException("User not found with provided ID"));
    }

    public void rewriteAllUsers(List<User> users) {
        List<String> strUsers = Utils.createListOfUsersAsStrings(users);
        try {
            Files.write(filePath, strUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(Long userId) {
        List<User> users = Utils.getAllUsers();
        List<User> newUsers = users.stream().filter(user -> !Objects.equals(user.getId(), userId)).toList();
        rewriteAllUsers(newUsers);
    }
}
