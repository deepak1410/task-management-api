package com.dpk.taskmanagement.auth;

import com.dpk.taskmanagement.auth.dto.AuthRequest;
import com.dpk.taskmanagement.auth.dto.AuthResponse;
import com.dpk.taskmanagement.auth.dto.RegisterRequest;
import com.dpk.taskmanagement.user.dto.ForgotPasswordRequest;
import com.dpk.taskmanagement.user.dto.ResetPasswordRequest;
import com.dpk.taskmanagement.user.entity.User;

public interface AuthService {
    User registerUser(RegisterRequest request);
    AuthResponse login(AuthRequest authRequest);
    void initiatePasswordReset(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    User verifyEmail(String token);
}
