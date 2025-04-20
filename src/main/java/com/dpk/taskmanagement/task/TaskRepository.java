package com.dpk.taskmanagement.task;

import com.dpk.taskmanagement.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatedBy(User user); // Custom query

}
