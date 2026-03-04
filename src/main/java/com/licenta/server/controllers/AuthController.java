package com.licenta.server.controllers;

import com.licenta.server.SecurityConfig.JwtUtil;
import com.licenta.server.dto.AddUserDTO;
import com.licenta.server.dto.LoginDTO;
import com.licenta.server.models.RefreshToken;
import com.licenta.server.services.RefreshTokenService;
import com.licenta.server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
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
}