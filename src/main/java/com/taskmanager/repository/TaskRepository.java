package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.Task.Status;
import com.taskmanager.model.Task.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedToId(Long userId);

    List<Task> findByStatus(Status status);

    List<Task> findByPriority(Priority priority);

    List<Task> findByAssignedToIdAndStatus(Long userId, Status status);

    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status != 'DONE'")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findByUserSortedByPriorityAndDueDate(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo.id = :userId AND t.status = :status")
    long countByUserAndStatus(@Param("userId") Long userId, @Param("status") Status status);
}
