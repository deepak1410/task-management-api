package com.dpk.taskmanagement.user.dto;

import com.dpk.taskmanagement.user.Role;
import com.dpk.taskmanagement.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private String username;
    private String email;
    private String name;
    private boolean emailVerified;
    private Role role;

    public static UserProfileDTO fromUser(User user) {
        return UserProfileDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .emailVerified(user.isEmailVerified())
                .role(user.getRole())
                .build();
    }
}
