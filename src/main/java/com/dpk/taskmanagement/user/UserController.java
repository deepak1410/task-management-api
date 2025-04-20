package com.dpk.taskmanagement.user;

import com.dpk.taskmanagement.user.dto.UpdateProfileDTO;
import com.dpk.taskmanagement.user.dto.UserProfileDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserServiceImpl userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getCurrentUserProfile(username));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateUserProfile(Authentication authentication,
                                                            @Valid @RequestBody UpdateProfileDTO updateProfileDTO) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.updateUserProfile(username, updateProfileDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProfileDTO>> listAllUsers() {
        log.debug("Attempting to get all the users");
        return ResponseEntity.ok(userService.listAllUsers());
    }

}
