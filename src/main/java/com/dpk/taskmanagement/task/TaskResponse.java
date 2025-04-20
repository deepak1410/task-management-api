package com.dpk.taskmanagement.task;

import java.time.LocalDateTime;


public record TaskResponse (
    Long id,
    String title,
    String description,
    String createdBy, // username
    LocalDateTime dueDate,
    boolean completed
){}
