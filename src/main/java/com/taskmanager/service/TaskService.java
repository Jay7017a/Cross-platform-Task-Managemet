package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Status;
import com.taskmanager.model.Task.Priority;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(String title, String description, Priority priority,
                           Long assignedUserId, LocalDateTime dueDate) {
        User user = userRepository.findById(assignedUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + assignedUserId));

        Task task = new Task(title, description, priority, user, dueDate);
        Task saved = taskRepository.save(task);
        LoggerUtil.info("Task created: [" + saved.getId() + "] " + saved.getTitle());
        return saved;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAssignedToId(userId);
    }

    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }

    public Task updateTaskStatus(Long taskId, Status newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        Status oldStatus = task.getStatus();
        task.setStatus(newStatus);
        Task updated = taskRepository.save(task);
        LoggerUtil.info("Task [" + taskId + "] status changed: " + oldStatus + " -> " + newStatus);
        return updated;
    }

    public Task updateTask(Long taskId, String title, String description,
                           Priority priority, LocalDateTime dueDate) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        if (title != null && !title.isBlank()) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (priority != null) task.setPriority(priority);
        if (dueDate != null) task.setDueDate(dueDate);

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
        LoggerUtil.info("Task deleted: " + taskId);
    }

    public long countTasksByUserAndStatus(Long userId, Status status) {
        return taskRepository.countByUserAndStatus(userId, status);
    }
}
