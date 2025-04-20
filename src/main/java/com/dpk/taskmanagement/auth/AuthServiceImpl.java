package com.dpk.taskmanagement.auth;

import com.dpk.taskmanagement.auth.dto.AuthRequest;
import com.dpk.taskmanagement.auth.dto.AuthResponse;
import com.dpk.taskmanagement.auth.dto.RegisterRequest;
import com.dpk.taskmanagement.auth.refreshtoken.RefreshTokenService;
import com.dpk.taskmanagement.common.ResourceNotFoundException;
import com.dpk.taskmanagement.email.EmailService;
import com.dpk.taskmanagement.email.EmailToken;
import com.dpk.taskmanagement.email.EmailTokenRepository;
import com.dpk.taskmanagement.user.Role;
import com.dpk.taskmanagement.user.TokenType;
import com.dpk.taskmanagement.user.UserRepository;
import com.dpk.taskmanagement.user.dto.ForgotPasswordRequest;
import com.dpk.taskmanagement.user.dto.ResetPasswordRequest;
import com.dpk.taskmanagement.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    public User registerUser(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .name(request.getName())
                .role(Role.USER) // Default role
                .enabled(false)
                .locked(false)
                .emailVerified(false)
                .build();
        return userRepository.save(user);
    }

    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        // Generating Access token and refresh token
        String accessToken = jwtService.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        // Save refreshToken
        refreshTokenService.saveToken(user, refreshToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public void initiatePasswordReset(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + request.getEmail()));

        log.debug("User has been found with email {}", request.getEmail());

        EmailToken token = EmailToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .type(TokenType.RESET_PASSWORD)
                .used(false)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        emailTokenRepository.save(token);
        log.debug("Email token has been saved");

        String resetUrl = frontendUrl + "/reset-password?token=" + token.getToken();
        String subject = "Reset your password";
        String body = "Click the link to reset the password: " + resetUrl;

        log.debug("Sending email for password reset");
        emailService.sendEmail(subject, body, user.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        EmailToken token = emailTokenRepository.findByTokenAndUsedFalseAndExpiryDateAfter(
                        request.getToken(), LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user); // Update user

        token.setUsed(true);
        emailTokenRepository.save(token); // Update EmailToken
    }

    @Override
    public User verifyEmail(String tokenStr) {
        EmailToken token = emailTokenRepository.findByTokenAndUsedFalseAndExpiryDateAfter(tokenStr, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

        User user = token.getUser();
        user.setEmailVerified(true);
        User verifiedUser = userRepository.save(user);

        token.setUsed(true);
        emailTokenRepository.save(token);
        return verifiedUser;
    }

}
