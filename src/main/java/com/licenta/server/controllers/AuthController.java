package com.licenta.server.controllers;

import com.licenta.server.SecurityConfig.JwtUtil;
import com.licenta.server.dto.AddUserDTO;
import com.licenta.server.dto.ChangePasswordRequestDTO;
import com.licenta.server.dto.LoginDTO;
import com.licenta.server.dto.UserStatsDTO;
import com.licenta.server.models.RefreshToken;
import com.licenta.server.models.User;
import com.licenta.server.repository.UserRepository;
import com.licenta.server.services.RefreshTokenService;
import com.licenta.server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private String getUsername() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return (String) principal;
    }



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AddUserDTO dto) {
        userService.addUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        return userService.login(dto);
    }

    // Add to AuthController


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String requestToken = body.get("refreshToken");
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(requestToken);

        String newAccessToken  = jwtUtil.generateToken(refreshToken.getUser().getUsername());
        String newRefreshToken = refreshTokenService.createRefreshToken(refreshToken.getUser()).getToken();

        return ResponseEntity.ok(Map.of(
                "accessToken",  newAccessToken,
                "refreshToken", newRefreshToken
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> body) {
        refreshTokenService.revokeRefreshToken(body.get("refreshToken"));
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/stats")
    public ResponseEntity<UserStatsDTO> getStats() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = getUsername();
        return ResponseEntity.ok(userService.getUserStats(username));
    }
    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "email", maskEmail(user.getEmail())
        ));
    }

    private String maskEmail(String email) {
        String[] parts = email.split("@");
        String local = parts[0];
        String masked = local.charAt(0) + "****" + local.charAt(local.length() - 1);
        return masked + "@" + parts[1];
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO dto) {
        userService.changePassword(getUsername(), dto);
        return ResponseEntity.ok("Password changed successfully!");
    }
}