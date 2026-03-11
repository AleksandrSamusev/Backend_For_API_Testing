package dev.practice.shopapp;

import dev.practice.shopapp.enums.AddressType;
import dev.practice.shopapp.models.Address;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // 1. Create Alex Developer
            User alex = new User();
            alex.setFirstName("Alex");
            alex.setLastName("Developer");
            alex.setEmail("alex.test@docker.local");
            alex.setPhoneNumber("+12345678901");

            Address home = new Address();
            home.setStreetAddress("123 Java Lane");
            home.setCity("Springfield");
            home.setCountryCode("US");
            home.setPostalCode("62704");
            home.setAddressType(AddressType.PRIMARY);

            alex.addAddress(home);
            userRepository.save(alex);

            // 2. Create Sarah Tester
            User sarah = new User();
            sarah.setFirstName("Sarah");
            sarah.setLastName("Tester");
            sarah.setEmail("sarah.tester@docker.local");
            sarah.setPhoneNumber("+19876543210");

            Address work = new Address();
            work.setStreetAddress("789 Pine Lane");
            work.setCity("Seattle");
            work.setCountryCode("US");
            work.setPostalCode("98101");
            work.setAddressType(AddressType.PRIMARY);

            sarah.addAddress(work);
            userRepository.save(sarah);

            System.out.println("✅ Local Docker database successfully seeded with test users!");
        };
    }
}
