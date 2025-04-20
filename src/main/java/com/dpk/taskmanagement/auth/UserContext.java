package com.dpk.taskmanagement.auth;

import com.dpk.taskmanagement.task.TaskController;
import com.dpk.taskmanagement.user.entity.User;
import com.dpk.taskmanagement.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class UserContext {
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final UserServiceImpl userService;

    /**
     * Helper method to centralize user resolution
     * @param userDetails
     * @return User instance
     */
    public User getUserFromPrincipal(UserDetails userDetails) {
        if (userDetails == null) {
            log.error("Unauthenticated request");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        return userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> {
                log.error("User not found: {}", userDetails.getUsername());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            });
    }
}
