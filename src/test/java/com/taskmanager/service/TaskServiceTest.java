package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Priority;
import com.taskmanager.model.Task.Status;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User mockUser;
    private Task mockTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User("john_doe", "john@example.com", "password123", User.Role.USER);
        mockUser.setId(1L);

        mockTask = new Task("Fix login bug", "Auth flow broken", Priority.HIGH, mockUser,
                LocalDateTime.now().plusDays(2));
        mockTask.setId(1L);
        mockTask.setStatus(Status.TODO);
    }

    @Test
    void testCreateTask_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task result = taskService.createTask("Fix login bug", "Auth flow broken",
                Priority.HIGH, 1L, LocalDateTime.now().plusDays(2));

        assertNotNull(result);
        assertEquals("Fix login bug", result.getTitle());
        assertEquals(Priority.HIGH, result.getPriority());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                taskService.createTask("Task", "Desc", Priority.LOW, 99L, null));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void testGetAllTasks_returnsList() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(mockTask));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("Fix login bug", tasks.get(0).getTitle());
    }

    @Test
    void testUpdateTaskStatus_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        mockTask.setStatus(Status.IN_PROGRESS);
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task updated = taskService.updateTaskStatus(1L, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, updated.getStatus());
        verify(taskRepository).save(mockTask);
    }

    @Test
    void testDeleteTask_success() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTask_notFound_throwsException() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> taskService.deleteTask(99L));
    }

    @Test
    void testGetTasksByUser_returnsList() {
        when(taskRepository.findByAssignedToId(1L)).thenReturn(Arrays.asList(mockTask));

        List<Task> tasks = taskService.getTasksByUser(1L);

        assertFalse(tasks.isEmpty());
        assertEquals(1L, tasks.get(0).getAssignedTo().getId());
    }
}
