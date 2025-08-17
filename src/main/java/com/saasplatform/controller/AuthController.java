package com.saasplatform.controller;

import com.saasplatform.dto.LoginRequest;
import com.saasplatform.entity.User;
import com.saasplatform.service.UserService;
import com.saasplatform.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            // Get user details from database
            User user = userService.findByEmail(loginRequest.getEmail());
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", jwt);
            response.put("refreshToken", tokenProvider.generateRefreshToken(user.getEmail()));
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "status", user.getStatus().toString(),
                "roles", user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList())
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is running");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody LoginRequest refreshRequest) {
        try {
            // Validate the refresh token
            if (tokenProvider.validateToken(refreshRequest.getRefreshToken())) {
                String email = tokenProvider.getUsernameFromToken(refreshRequest.getRefreshToken());
                User user = userService.findByEmail(email);
                
                if (user == null) {
                    return ResponseEntity.badRequest().body("User not found");
                }

                // Generate new access token
                String newAccessToken = tokenProvider.generateToken(email);
                
                Map<String, Object> response = new HashMap<>();
                response.put("accessToken", newAccessToken);
                response.put("user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "status", user.getStatus().toString(),
                    "roles", user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList())
                ));

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Invalid refresh token");
            }
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.badRequest().body("Token refresh failed");
        }
    }
}

