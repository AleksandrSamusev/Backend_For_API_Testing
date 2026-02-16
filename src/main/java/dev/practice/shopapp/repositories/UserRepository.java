package dev.practice.shopapp.repositories;

import dev.practice.shopapp.dto.UserCreateDTO;
import dev.practice.shopapp.exceptions.ResourceNotFoundException;
import dev.practice.shopapp.mappers.UserMapper;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class UserRepository {

    private static final Path filePath = Path.of("users.txt");

    public User saveUser(UserCreateDTO dto) {
        // 1. Sanitize the input email immediately
        String emailToCheck = dto.getEmail().trim().toLowerCase();

        try {
            // 2. Read all lines from your storage file
            List<String> lines = Files.readAllLines(filePath);

            // 3. PRO CHECK: Stream through lines and check index 3
            boolean emailExists = lines.stream()
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 4) // Safety check: line must have at least 4 parts
                    .anyMatch(parts -> parts[3].trim().equalsIgnoreCase(emailToCheck));

            if (emailExists) {
                // Throw an exception that maps to your JSON "error" field
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }

            // 4. If unique, proceed with save logic
            Long id = Utils.generateId();
            User user = UserMapper.toUser(dto, id);
            String userAsString = Utils.createUserString(user);

            lines.add(userAsString);
            Files.write(filePath, lines);
            return user;

        } catch (IOException e) {
            throw new RuntimeException("Database storage failure", e);
        }
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
