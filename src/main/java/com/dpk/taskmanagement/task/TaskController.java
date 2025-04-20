package com.dpk.taskmanagement.task;

import com.dpk.taskmanagement.auth.UserContext;
import com.dpk.taskmanagement.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name="Tasks", description = "Manage user tasks")
@RestController
@RequestMapping(path = "/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserContext userContext;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Operation(summary = "Create a new task")
    @SecurityRequirement(name="bearerAuth")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest taskRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = userContext.getUserFromPrincipal(userDetails);
        log.debug("Creating a task for user {}", user.getUsername());

        TaskResponse taskResponse = taskService.createTask(taskRequest, user);
        return ResponseEntity.created(URI.create("/tasks" + taskResponse.id()))
                .body(taskResponse);
    }

    @GetMapping
    public List<TaskResponse> getUserTasks(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userContext.getUserFromPrincipal(userDetails);
        log.debug("Fetching tasks for user {}", user.getUsername());
        return taskService.getUserTasks(user);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") Long id) {
        log.debug("Fetching task with id {}", id);
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable("id") Long id) {
        log.debug("Attempting to delete task with id {}", id);
        taskService.deleteTask(id);
        log.debug("Successfully deleted task with id {}", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody @Valid TaskRequest taskRequest,
                                                   @PathVariable("id") Long id) {
        log.debug("Attempting to update task with id {}", id);
        TaskResponse taskResponse = taskService.updateTask(id, taskRequest);
        log.debug("Successfully updated a task with id {}", id);
        return ResponseEntity.ok(taskResponse);
    }

}
