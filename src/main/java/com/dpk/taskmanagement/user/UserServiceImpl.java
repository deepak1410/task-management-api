package com.dpk.taskmanagement.user;

import com.dpk.taskmanagement.common.ResourceNotFoundException;
import com.dpk.taskmanagement.user.dto.UpdateProfileDTO;
import com.dpk.taskmanagement.user.dto.UserProfileDTO;
import com.dpk.taskmanagement.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserProfileDTO getCurrentUserProfile(String username) {
        log.debug("Attempting to get user profile for {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User with username " + username + " not found"));

        return UserProfileDTO.fromUser(user);
    }

    public UserProfileDTO updateUserProfile(String username, UpdateProfileDTO updateProfileDTO) {
        User existingUser = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User with username " + username + " not found"));

        if(updateProfileDTO.getEmail() != null) {
            existingUser.setEmail(updateProfileDTO.getEmail());
        }

        if(updateProfileDTO.getName() != null) {
            existingUser.setName(updateProfileDTO.getName());
        }

        User user = userRepository.save(existingUser);
        return UserProfileDTO.fromUser(user);
    }

    public List<UserProfileDTO> listAllUsers() {
        return userRepository.findAll().stream()
                .map(UserProfileDTO::fromUser)
                .toList();
    }

}
