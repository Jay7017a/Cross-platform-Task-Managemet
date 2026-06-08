package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Priority;
import com.taskmanager.model.Task.Status;
import com.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // GET task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET tasks by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    // GET tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    // GET tasks by priority
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable Priority priority) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority));
    }

    // GET overdue tasks
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }

    // POST create task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> payload) {
        String title       = (String) payload.get("title");
        String description = (String) payload.get("description");
        Priority priority  = Priority.valueOf((String) payload.get("priority"));
        Long userId        = Long.valueOf(payload.get("userId").toString());
        LocalDateTime due  = payload.get("dueDate") != null
                ? LocalDateTime.parse((String) payload.get("dueDate")) : null;

        Task task = taskService.createTask(title, description, priority, userId, due);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    // PUT update task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody Map<String, Object> payload) {
        String title       = (String) payload.get("title");
        String description = (String) payload.get("description");
        Priority priority  = payload.get("priority") != null
                ? Priority.valueOf((String) payload.get("priority")) : null;
        LocalDateTime due  = payload.get("dueDate") != null
                ? LocalDateTime.parse((String) payload.get("dueDate")) : null;

        Task updated = taskService.updateTask(id, title, description, priority, due);
        return ResponseEntity.ok(updated);
    }

    // PATCH update status only
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id,
                                             @RequestBody Map<String, String> payload) {
        Status newStatus = Status.valueOf(payload.get("status"));
        return ResponseEntity.ok(taskService.updateTaskStatus(id, newStatus));
    }

    // DELETE task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // Exception handler
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}
