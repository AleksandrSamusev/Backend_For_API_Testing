package dev.practice.shopapp.services;
import dev.practice.shopapp.dto.JwtResponse;
import dev.practice.shopapp.dto.LoginRequest;
import dev.practice.shopapp.dto.RegisterRequest;
import dev.practice.shopapp.enums.UserRole;
import dev.practice.shopapp.models.Role;
import dev.practice.shopapp.models.User;
import dev.practice.shopapp.repositories.RoleRepository;
import dev.practice.shopapp.repositories.UserRepository;
import dev.practice.shopapp.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    // 🚀 INITIATE ACCESS: The Login Handshake
    public JwtResponse login(LoginRequest loginRequest) {
        // 1. Verify credentials with the AuthenticationManager (The Handshake)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // 2. Set security context and generate the digital "Clearance Badge" (The Token)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 3. Extract the UserDetails from the principal
        User userDetails = (User) authentication.getPrincipal();

        // 🚀 THE FIX: Convert authorities to a List of Strings (e.g., ["ROLE_ADMIN"])
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 4. Return the finalized badge for the Security Terminal
        return new JwtResponse(jwt, userDetails.getEmail(), roles);
    }

    // 🚀 FORGE CREDENTIALS: The Registration Process
    public String register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new commander account
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());

        // 🛡️ BCRYPT ENCODING: Never store plain text ciphers
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        // Assign default Showroom clearance (ROLE_USER)
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        return "User registered successfully!";
    }
}
