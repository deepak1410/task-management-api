package com.dpk.taskmanagement.task;

import com.dpk.taskmanagement.common.ResourceNotFoundException;
import com.dpk.taskmanagement.mapper.AppMapper;
import com.dpk.taskmanagement.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final AppMapper appMapper;

    public TaskResponse createTask(TaskRequest taskRequest, User creator) {
        Task task = appMapper.taskRequestToTask(taskRequest); // DTO -> Entity
        task.setCreatedBy(creator); // Manually set fields not in DTO
        return appMapper.taskToTaskResponse(taskRepository.save(task)); // Entity -> DTO
    }

    public List<TaskResponse> getUserTasks(User user) {
        return taskRepository.findByCreatedBy(user).stream()
                .map(appMapper::taskToTaskResponse)
                .toList();
    }

    public TaskResponse getTask(Long id) {
        return taskRepository.findById(id)
                .map(appMapper::taskToTaskResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // Update the existingTask
        if(StringUtils.isNotBlank(taskRequest.title())) {
            existingTask.setTitle(taskRequest.title());
        }

        if(StringUtils.isNotBlank(taskRequest.description())) {
            existingTask.setDescription(taskRequest.description());
        }

        if(taskRequest.dueDate() != null) {
            existingTask.setDueDate(taskRequest.dueDate());
        }

        return appMapper.taskToTaskResponse(taskRepository.save(existingTask)); // Entity -> DTO
    }

}
