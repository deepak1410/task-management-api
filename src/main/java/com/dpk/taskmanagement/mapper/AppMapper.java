package com.dpk.taskmanagement.mapper;

import com.dpk.taskmanagement.task.Task;
import com.dpk.taskmanagement.task.TaskRequest;
import com.dpk.taskmanagement.task.TaskResponse;
import com.dpk.taskmanagement.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AppMapper {

    // Map TaskRequest -> Task
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completed", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Task taskRequestToTask(TaskRequest taskRequest);

    // Map Task -> TaskResponse
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserToUsername")
    TaskResponse taskToTaskResponse(Task task);

    @Named("mapUserToUsername")
    default String mapUserToUsername(User createdBy) {
        return createdBy != null ? createdBy.getUsername() : null;
    }
}
