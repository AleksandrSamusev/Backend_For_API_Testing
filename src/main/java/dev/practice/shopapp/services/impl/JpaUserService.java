package dev.practice.shopapp.services.impl;

import dev.practice.shopapp.dto.AddressDto;
import dev.practice.shopapp.enums.AddressType;
import dev.practice.shopapp.enums.SortingOptions;
import dev.practice.shopapp.dto.UserCreateDto;
import dev.practice.shopapp.dto.UserUpdateDto;
import dev.practice.shopapp.exceptions.ResourceNotFoundException;
import dev.practice.shopapp.mappers.AddressMapper;
import dev.practice.shopapp.mappers.UserMapper;
import dev.practice.shopapp.models.Address;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.repositories.UserRepository;
import dev.practice.shopapp.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class JpaUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Value("${app.limits.user.max-addresses}")
    private int maxAddresses;

    public User createUser(UserCreateDto dto) {
        User user = userMapper.toUser(dto);
        User savedUser = userRepository.save(user);
        log.info("User with id {} created", savedUser.getId());
        return savedUser;
    }

    public Page<User> getAllUsers(String search, int page, int size, SortingOptions option) {
        Sort sort = switch (option) {
            case FIRST_NAME_ASC -> Sort.by("firstName").ascending();
            case FIRST_NAME_DESC -> Sort.by("firstName").descending();
            case LAST_NAME_ASC -> Sort.by("lastName").ascending();
            case LAST_NAME_DESC -> Sort.by("lastName").descending();
            case ID_DESC -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };

        Pageable pageable = PageRequest.of(page, size, sort);
        if(search != null && !search.trim().isEmpty()) {
            return userRepository.findBySearchTerm(search.toLowerCase(), pageable);
        }
        return userRepository.findAllWithAddresses(pageable);
    }

    public User getUserById(Long userId) {
        return userRepository.findByIdWithAddresses(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with given id: " + userId));
    }

    public User updateUser(Long userId, UserUpdateDto dto) {
        // 1. Fetch from DB (Hibernate now "tracks" this object)
        User user = userRepository.findByIdWithAddresses(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with given ID " + userId));

        // 2. Optimized Email Check (Direct DB lookup instead of streaming all users)
        if (dto.getEmail() != null) {
            String newEmail = dto.getEmail().strip().toLowerCase();
            userRepository.findByEmail(newEmail).ifPresent(existing -> {
                if (!existing.getId().equals(userId)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
                }
            });
            user.setEmail(newEmail);
        }

        // 3. Update Simple Fields with Blank Checks
        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            user.setFirstName(dto.getFirstName().strip());
        }

        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            user.setLastName(dto.getLastName().strip());
        }

        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(dto.getPhoneNumber().strip());
        }

        // 4. Update Addresses (Replace strategy enabled by orphanRemoval = true)
        if (dto.getAddresses() != null) {
            user.getAddresses().clear();  // Triggers automatic DELETE for old ones
            dto.getAddresses().forEach(addressDto -> {
                Address newAddress = addressMapper.toAddress(addressDto);
                user.addAddress(newAddress);
            });
        }
        log.info("User with id {} successfully updated", userId);
        return user;
    }

    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with given ID " + userId));
        userRepository.delete(user);
        return "User with ID: " + userId + " successfully deleted";
    }

    @Override
    public User addAddress(Long userId, AddressDto dto) {
        // Fetch user and addresses in one query
        User user = userRepository.findByIdWithAddresses(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with a given ID: " + userId)
        );

        // Check the maximum addresses
        if (user.getAddresses().size() >= maxAddresses) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum " + maxAddresses + " addresses allowed!");
        }

        // If new address of type PRIMARY or BILLING, demote existing addresses to shipping
        if (dto.getAddressType() == AddressType.PRIMARY || dto.getAddressType() == AddressType.BILLING) {
            user.getAddresses().stream()
                    .filter(a -> a.getAddressType() == dto.getAddressType())
                    .forEach(a -> a.setAddressType(AddressType.SHIPPING));
        }

        // Map, link and save
        Address newAddress = addressMapper.toAddress(dto);
        user.addAddress(newAddress);
        return userRepository.save(user);
    }

    public User removeAddress(Long userId, Long addressId) {
        // Fetching the user with addresses and find proper address to delete
        User user = userRepository.findByIdWithAddresses(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with given ID: " + userId)
        );

        // Check if it is the only one address (cannot delete if it is only one)
        if (user.getAddresses().size() <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must have at least one address");
        }

        // Find the specific address in the collection of addresses
        Address addressToRemove = user.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with a given ID: " + addressId));
        user.removeAddress(addressToRemove);

        boolean hasPrimary = user.getAddresses().stream()
                .anyMatch(a -> a.getAddressType() == AddressType.PRIMARY);

        if (!hasPrimary && !user.getAddresses().isEmpty()) {
            // If we just deleted the primary, promote the first available one
            user.getAddresses().getFirst().setAddressType(AddressType.PRIMARY);
        }
        return userRepository.save(user);
    }

    @Override
    public User promoteAddress(Long userId, Long addressId) {
        User user = userRepository.findByIdWithAddresses(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with a given ID: " + userId));

        Address address = user.getAddresses().stream()
                .filter(a-> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Address not found with a given ID: " + addressId));

        if(address.getAddressType() == AddressType.PRIMARY) {
            return user;
        }
        user.getAddresses().forEach(a -> {
            if(a.getAddressType()==AddressType.PRIMARY) {
                a.setAddressType(AddressType.SHIPPING);
            }
        });
        address.setAddressType(AddressType.PRIMARY);
        return userRepository.save(user);
    }
}
