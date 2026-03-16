package dev.practice.shopapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String email, List<String> roles) {
        this.token = accessToken;
        this.email = email;
        this.roles = roles;
    }
}
