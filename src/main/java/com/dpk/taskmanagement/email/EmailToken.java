package com.dpk.taskmanagement.email;

import com.dpk.taskmanagement.user.TokenType;
import com.dpk.taskmanagement.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private TokenType type;

    @ManyToOne
    private User user;

    private LocalDateTime expiryDate;
    private boolean used;
}
