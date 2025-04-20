package com.dpk.taskmanagement.user;

import com.dpk.taskmanagement.auth.dto.RegisterRequest;
import com.dpk.taskmanagement.user.dto.ForgotPasswordRequest;
import com.dpk.taskmanagement.user.dto.ResetPasswordRequest;
import com.dpk.taskmanagement.user.dto.UpdateProfileDTO;
import com.dpk.taskmanagement.user.dto.UserProfileDTO;
import com.dpk.taskmanagement.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public Optional<User> findByUsername(String username);
    public UserProfileDTO getCurrentUserProfile(String username);
    public UserProfileDTO updateUserProfile(String username, UpdateProfileDTO updateProfileDTO);
    public List<UserProfileDTO> listAllUsers();
}
