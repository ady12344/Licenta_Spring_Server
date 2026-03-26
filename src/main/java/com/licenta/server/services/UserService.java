package com.licenta.server.services;

import ch.qos.logback.core.boolex.Matcher;
import com.licenta.server.SecurityConfig.JwtUtil;
import com.licenta.server.dto.AddUserDTO;
import com.licenta.server.dto.ChangePasswordRequestDTO;
import com.licenta.server.dto.LoginDTO;
import com.licenta.server.dto.UserStatsDTO;
import com.licenta.server.exception.UserAlreadyExistsException;
import com.licenta.server.models.MediaType;
import com.licenta.server.models.User;
import com.licenta.server.repository.ReviewRepository;
import com.licenta.server.repository.UserLibraryRepository;
import com.licenta.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ReviewRepository reviewRepository;
    private final UserLibraryRepository libraryRepository;
    public void  addUser(AddUserDTO dto) {
        if(userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists!");
        }
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists!");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        userRepository.save(user);
    }
    // Add RefreshTokenService injection
    private final RefreshTokenService refreshTokenService;

    // Update login() method
    public ResponseEntity<?> login(LoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String accessToken  = jwtUtil.generateToken(user.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return ResponseEntity.ok(Map.of(
                "accessToken",  accessToken,
                "refreshToken", refreshToken
        ));
    }

    public UserStatsDTO getUserStats(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        long totalMovies    = libraryRepository.countByUserIdAndMediaType(user.getId(), MediaType.MOVIE);
        long totalTvShows   = libraryRepository.countByUserIdAndMediaType(user.getId(), MediaType.TV);
        long totalReviews   = reviewRepository.countByUserId(user.getId());
        long positiveReviews = reviewRepository.countByUserIdAndLiked(user.getId(), true);
        long negativeReviews = reviewRepository.countByUserIdAndLiked(user.getId(), false);

        return UserStatsDTO.builder()
                .username(username)
                .totalMovies(totalMovies)
                .totalTvShows(totalTvShows)
                .totalReviews(totalReviews)
                .positiveReviews(positiveReviews)
                .negativeReviews(negativeReviews)
                .build();
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequestDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect!");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
