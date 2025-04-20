package com.dpk.taskmanagement.auth.refreshtoken;

import com.dpk.taskmanagement.auth.JwtService;
import com.dpk.taskmanagement.auth.dto.AuthResponse;
import com.dpk.taskmanagement.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.refreshTokenExpiryMs}")
    private long refreshTokenExpiryMs; // Used for generating new access token

    public void saveToken(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plus(refreshTokenExpiryMs, ChronoUnit.MILLIS))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> getValidToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(rt -> !rt.isRevoked() && rt.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    public void revokeToken(String token, String currentUsername) throws AccessDeniedException {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if(!refreshToken.getUser().getUsername().equalsIgnoreCase(currentUsername)) {
            throw new AccessDeniedException("You can't revoke someone else's token");
        }

        if(refreshToken.isRevoked()) {
            throw new IllegalArgumentException("Token is already revoked");
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse refreshToken(String refreshTokenStr) {
        String username = jwtService.extractUsername(refreshTokenStr);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Optional<RefreshToken> storedToken = getValidToken(refreshTokenStr);
        if(storedToken.isEmpty() || storedToken.get().isRevoked()) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(userDetails.getUsername());
        return new AuthResponse(newAccessToken, refreshTokenStr);
    }
}
