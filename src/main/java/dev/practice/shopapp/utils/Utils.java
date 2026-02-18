package dev.practice.shopapp.utils;

import dev.practice.shopapp.models.Address;
import dev.practice.shopapp.models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final Path filePath = Path.of("users.txt");

    public static String createUserString(User user) {
        // id,firstName,lastName,email,phoneNumber,street,apartment,city,state,postalCode,countryCode,createdAt,updatedAt

        return user.getId() + "," +
                user.getFirstName() + "," +
                user.getLastName() + "," +
                user.getEmail() + "," +
                user.getPhoneNumber() + "," +
                user.getAddress().getStreetAddress() + "," +
                user.getAddress().getApartment() + "," +
                user.getAddress().getCity() + "," +
                user.getAddress().getState() + "," +
                user.getAddress().getPostalCode() + "," +
                user.getAddress().getCountryCode() + "," +
                user.getAddress().getCreatedAt() + "," +
                user.getAddress().getUpdatedAt();
    }

    public static List<String> createListOfUsersAsStrings(List<User> users) {
        List<String> strUsers = new ArrayList<>();
        for (User user : users) {
            strUsers.add(createUserString(user));
        }
        return strUsers;
    }

    public static long generateId() {
        return Instant.now().toEpochMilli();
    }

    public static List<User> getAllUsers() {

        // id,firstName,lastName,email,phoneNumber,street,apartment,city,state,postalCode,countryCode,createdAt,updatedAt
        List<User> users = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] sl = line.split(",");

                // Safety Check: Ensure the line has all 13 columns
                if (sl.length < 13) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                Address address = new Address(sl[5], sl[6], sl[7], sl[8], sl[9],
                        sl[10], LocalDateTime.parse(sl[11]), LocalDateTime.parse(sl[12]));
                User user = new User(Long.parseLong(sl[0]), sl[1], sl[2], sl[3], sl[4], address);
                users.add(user);
            }
        } catch (IOException e) {
            System.err.println("Error parsing user data: " + e.getMessage());
        }
        return users;
    }

}
