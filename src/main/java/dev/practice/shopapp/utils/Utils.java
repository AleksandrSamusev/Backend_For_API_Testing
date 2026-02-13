package dev.practice.shopapp.utils;

import dev.practice.shopapp.models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final Path filePath = Path.of("users.txt");

    public static String createUserString(User user) {

        return user.getId() + "," +
                user.getFirstName() + "," +
                user.getLastName() + "," +
                user.getEmail() + "," +
                user.getPhoneNumber();
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
        List<User> users = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String[] sl = lines.get(i).split(",");
                User user = new User(Long.parseLong(sl[0]), sl[1], sl[2], sl[3], sl[4]);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

}
